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

/**
 * Goal {@code archboot:init}: generates a new Spring Boot project skeleton.
 *
 * <p>The goal validates and normalizes the user input, creates the output directory,
 * writes a {@code scaffold.yml} descriptor and delegates the creation of the project
 * structure to the {@link ArchitectureProcessor} that matches the requested architecture.</p>
 *
 * <p>It does not require an existing Maven project, so it can be run from any directory:</p>
 * <pre>{@code
 * mvn com.jagt.archboot.plugin:archboot-maven-plugin:init \
 *     -DgroupId=com.example -DartifactId=my-app -Darchitecture=mvc
 * }</pre>
 *
 */
@Mojo(name = "init", requiresProject = false)
public class InitProjectMojo extends InitProjectAbstractMojo {

    /**
     * Executes the goal.
     *
     * @throws MojoExecutionException if the architecture or configuration type is invalid,
     *                                the output directory already exists or cannot be created,
     *                                or the project generation fails
     * @throws MojoFailureException   declared by the {@link org.apache.maven.plugin.Mojo} contract
     */
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
        validateVersions(scaffold);
        generateScaffoldYaml(scaffold, projectDir);

        ArchitectureProcessor processor = ArchitectureProcessorFactory.get(architectureType);
        processor.execute(scaffold, projectDir, getLog());

        getLog().info("> [Mojo] Successfully building architecture structure");
    }

    /**
     * Fills in defaults and normalizes the user input: {@code name} falls back to the
     * artifact id, the artifact id is converted to kebab-case and the package name is
     * derived from {@code groupId} and {@code artifactId} when not provided.
     */
    private void normalizeInputs() {
        getLog().info("> [Mojo] Normalizing inputs");

        if (StringUtils.isBlank(name)) {
            name = artifactId;
        }

        artifactId = Normalizer.normalize(artifactId, NormalizationType.ARTIFACT_ID);

        if (StringUtils.isBlank(packageName)) {
            String packageBase = groupId + "." + artifactId;
            packageName = Normalizer.normalize(packageBase, NormalizationType.PACKAGE_NAME);
        }
    }

    /**
     * Resolves and creates the project directory ({@code <output>/<artifactId>}).
     *
     * @return the project directory, or the base directory itself when it is a filesystem root
     * @throws MojoExecutionException if the directory already exists or cannot be created
     */
    private File validateOutputDir() throws MojoExecutionException {
        getLog().info("> [Mojo] Validating output directory :: " + outputDir);
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

    /**
     * Builds the {@link ScaffoldModel} from the current Mojo parameters.
     *
     * @param architecture  the parsed architecture type
     * @param configuration the parsed configuration file type
     * @return the populated scaffold model
     */
    private ScaffoldModel generateScaffoldModel(ArchitectureType architecture, ConfigApplicationType configuration) {
        getLog().info("> [Mojo] Generating scaffold model");

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

    /**
     * Writes the {@code scaffold.yml} descriptor into the project directory.
     *
     * @param scaffoldModel the model to serialize
     * @param projectDir    the target project directory
     * @throws MojoExecutionException if the file cannot be written
     */
    private void generateScaffoldYaml(ScaffoldModel scaffoldModel, File projectDir) throws MojoExecutionException {
        try {
            getLog().info("> [Mojo] Generating scaffold.yml...");
            YamlReader.write(scaffoldModel, projectDir);
        } catch (IOException e) {
            throw new MojoExecutionException("[ERROR] Error creating project: " + e.getMessage(), e);
        }
    }

    /**
     * Validates the Java's and Spring Boot's versions in the scaffold model.
     *
     * @param scaffoldModel the model to serialize
     * @throws MojoExecutionException if the version is invalid
     */
    private void validateVersions(ScaffoldModel scaffoldModel) throws MojoExecutionException {
        String javaVersion = scaffoldModel.getData().getConfig().getJavaVersion();
        String springBootVersion = scaffoldModel.getData().getConfig().getSpringBootVersion();

        try {
            getLog().info("> [Mojo] Checking Java's version is supported...");
            int javaIntVersion = Integer.parseInt(javaVersion);

            if (ConstantsPlugin.JAVA_VERSION_SUPPORTED > javaIntVersion) {
                throw new MojoExecutionException("[ERROR] Java version not supported: " + javaVersion);
            }
        } catch (NumberFormatException e) {
            throw new MojoExecutionException("[ERROR] Invalid Java version: " + javaVersion);
        }


        try {
            getLog().info("> [Mojo] Checking Spring Boot's version is supported...");
            String[] versionParts = springBootVersion.split("\\.");

            if (versionParts.length < 2) {
                throw new MojoExecutionException("[ERROR] Invalid Spring Boot version: " + springBootVersion);
            }

            int springBootMajorVersion = Integer.parseInt(versionParts[0]);

            if (ConstantsPlugin.SPRING_VERSION_SUPPORTED > springBootMajorVersion) {
                throw new MojoExecutionException("[ERROR] Spring Boot version not supported: " + springBootVersion);
            }
        } catch (NumberFormatException e) {
            throw new MojoExecutionException("[ERROR] Invalid Spring Boot version: " + springBootVersion);
        }
    }
}
