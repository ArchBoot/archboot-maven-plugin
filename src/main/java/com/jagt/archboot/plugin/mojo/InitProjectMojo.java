package com.jagt.archboot.plugin.mojo;

import com.jagt.archboot.plugin.model.AnnotationModel;
import com.jagt.archboot.plugin.model.ConfigModel;
import com.jagt.archboot.plugin.model.DataModel;
import com.jagt.archboot.plugin.model.ScaffoldModel;
import com.jagt.archboot.plugin.model.enums.ArchitectureType;
import com.jagt.archboot.plugin.model.enums.ConfigApplicationType;
import com.jagt.archboot.plugin.model.enums.NormalizationType;
import com.jagt.archboot.plugin.mojo.abstracts.InitProjectAbstractMojo;
import com.jagt.archboot.plugin.processor.ArchitectureProcessor;
import com.jagt.archboot.plugin.processor.factory.ArchitectureProcessorFactory;
import com.jagt.archboot.plugin.utils.ConstantsPlugin;
import com.jagt.archboot.plugin.utils.Normalizer;
import com.jagt.archboot.plugin.utils.YamlReader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Mojo(name = "init", requiresProject = false)
public class InitProjectMojo extends InitProjectAbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ArchitectureType architectureType = ArchitectureType.fromString(architecture);
        if (architectureType == null) {
            throw new MojoExecutionException("[ERROR] Invalid architecture type: " + architecture);
        }

        ConfigApplicationType configuration = ConfigApplicationType.fromString(configurationType);
        if (configuration == null) {
            throw new MojoExecutionException("[ERROR] Invalid configuration type: " + configurationType);
        }

        normalizeInputs();
        File projectDir = validateOutputDir();

        ScaffoldModel scaffold = generateScaffoldModel(architectureType, configuration);
        generateScaffoldYaml(scaffold, projectDir);

        ArchitectureProcessor processor = ArchitectureProcessorFactory.get(architectureType);
        processor.execute(scaffold, projectDir, getLog());
    }

    private void normalizeInputs() {
        getLog().info("> Normalizing inputs");

        if (StringUtils.isBlank(name)) {
            name = artifactId;
        }

        artifactId = Normalizer.normalize(artifactId, NormalizationType.ARTIFACT_ID);

        if (StringUtils.isBlank(packageName)) {
            String packageBase = groupId + "." + artifactId;
            packageName = Normalizer.normalize(packageBase, NormalizationType.PACKAGE_NAME);
        }
    }

    private File validateOutputDir() throws MojoExecutionException {
        getLog().info("> Validating output directory :: " + outputDir);
        File baseDir = (outputDir == null)
                ? new File(System.getProperty(ConstantsPlugin.USER_DIR))
                : outputDir;
        String output = baseDir.getPath();

        if (ConstantsPlugin.CLASSPATH_SEPARATOR.equals(output) || "\\".equals(output)) {
            return baseDir;
        }

        File projectDir = new File(baseDir, artifactId);

        if (projectDir.exists()) {
            throw new MojoExecutionException("[ERROR] Output directory already exists: " + projectDir.getAbsolutePath());
        }

        try {
            Files.createDirectories(projectDir.toPath());
        } catch (IOException e) {
            throw new MojoExecutionException("[ERROR] Failed to create output directory: " + projectDir.getAbsolutePath(), e);
        }

        return projectDir;
    }

    private ScaffoldModel generateScaffoldModel(ArchitectureType architecture, ConfigApplicationType configuration) {
        getLog().info("> Generating scaffold model");

        AnnotationModel annotation = AnnotationModel.builder()
                .lombok(lombok)
                .mapstruct(mapstruct)
                .build();

        ConfigModel config = ConfigModel.builder()
                .extension(configuration)
                .gitKeep(gitKeep)
                .javaVersion(javaVersion)
                .springBootVersion(springVersion)
                .build();

        DataModel data = DataModel.builder()
                .artifactId(artifactId)
                .groupId(groupId)
                .version(version)
                .name(name)
                .packageName(packageName)
                .description(description)
                .annotation(annotation)
                .config(config)
                .build();
        return ScaffoldModel.builder()
                .data(data)
                .architecture(architecture)
                .build();
    }

    private void generateScaffoldYaml(ScaffoldModel scaffoldModel, File projectDir) throws MojoExecutionException {
        try {
            getLog().info("> Generating scaffold.yml...");
            YamlReader.write(scaffoldModel, projectDir);
        } catch (IOException e) {
            throw new MojoExecutionException("[ERROR] Error creating project: " + e.getMessage(), e);
        }
    }
}
