package com.jagt.archboot.plugin.processor.abstracts;

import com.jagt.archboot.plugin.generator.FreeMarkerGenerator;
import com.jagt.archboot.plugin.generator.PomGenerator;
import com.jagt.archboot.plugin.generator.RawGenerator;
import com.jagt.archboot.plugin.model.DataModel;
import com.jagt.archboot.plugin.model.ScaffoldModel;
import com.jagt.archboot.plugin.model.enums.NormalizationType;
import com.jagt.archboot.plugin.processor.ArchitectureProcessor;
import com.jagt.archboot.plugin.utils.ConstantsPlugin;
import com.jagt.archboot.plugin.utils.Normalizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ArchitectureProcessorAbstract implements ArchitectureProcessor {
    protected final PomGenerator pomGenerator = new PomGenerator();

    @Override
    public void execute(ScaffoldModel scaffoldModel, File projectDir, Log log) throws MojoExecutionException {
        try {
            DataModel dataModel = scaffoldModel.getData();

            log.info("  > [Processor] Building architecture structure for " + variantLabel() + " variant");
            buildStructure(scaffoldModel, projectDir, log);

            File mainSrc = resolveMainSourceDir(projectDir, dataModel);
            log.info("> [Processor] Generating main class...");
            generateMainClass(dataModel, mainSrc);

            File resourcesDir = resolveResourcesDir(projectDir, dataModel);
            log.info("> [Processor] Generating application resource...");
            generateResources(dataModel, resourcesDir);

            log.info("> [Processor] Generating left resources...");
            generateItemsBasic(projectDir, dataModel, log);

            if (dataModel.getConfig() != null && dataModel.getConfig().isGitKeep()) {
                log.info("> [Processor] Generating .gitkeep files...");
                generateGitKeep(projectDir, log);
            }

        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    protected abstract void buildStructure(ScaffoldModel config, File projectDir, Log log) throws IOException;

    protected abstract File resolveMainSourceDir(File projectDir, DataModel dataModel) throws IOException;

    protected abstract File resolveResourcesDir(File projectDir, DataModel dataModel) throws IOException;

    protected abstract String variantLabel();

    protected void generateMainClass(DataModel dataModel, File srcDir) throws IOException {
        String className = Normalizer.normalize(
                dataModel.getArtifactId(), NormalizationType.CLASS_NAME
        );

        Map<String, Object> model = new HashMap<>();
        model.put(ConstantsPlugin.PACKAGE_NAME, dataModel.getPackageName());
        model.put(ConstantsPlugin.CLASS_NAME, className);

        FreeMarkerGenerator.generate(
                ConstantsPlugin.APP_JAVA_SHARED,
                model,
                new File(srcDir, className + ConstantsPlugin.DOT_JAVA)
        );
    }

    protected void generateResources(DataModel dataModel, File resourcesDir) throws IOException {

        String configFile = ConstantsPlugin.YAML.equalsIgnoreCase(dataModel.getConfig().getExtension().name())
                ? ConstantsPlugin.APPLICATION_YML : ConstantsPlugin.APPLICATION_PROPERTIES;

        Map<String, Object> model = new HashMap<>();

        FreeMarkerGenerator.generate(
                ConstantsPlugin.SHARED + ConstantsPlugin.CLASSPATH_SEPARATOR + configFile + ConstantsPlugin.DOT_FTL,
                model,
                new File(resourcesDir, configFile)
        );
    }

    protected void generateItemsBasic(File projectDir, DataModel dataModel, Log log) throws IOException {
        log.info("  > [Processor] Generating .gitignore...");
        RawGenerator.copy(ConstantsPlugin.GITIGNORE_RAW_SHARED, new File(projectDir, ConstantsPlugin.DOT_GITIGNORE));

        log.info("  > [Processor] Generating .gitattributes...");
        RawGenerator.copy(ConstantsPlugin.GITATTRIBUTES_RAW_SHARED, new File(projectDir, ConstantsPlugin.DOT_GITATTRIBUTES));

        log.info("  > [Processor] Generating mvnw...");
        RawGenerator.copy(ConstantsPlugin.MVNW_RAW_SHARED, new File(projectDir, ConstantsPlugin.MVNW));

        log.info("  > [Processor] Generating mvnw.cmd...");
        RawGenerator.copy(ConstantsPlugin.MVNW_CMD_RAW_SHARED, new File(projectDir, ConstantsPlugin.MVNW_CMD));

        log.info("  > [Processor] Generating .mvn/wrapper/maven-wrapper.properties...");
        RawGenerator.copy(ConstantsPlugin.MVNW_PROPERTIES_RAW_SHARED, new File(projectDir, ConstantsPlugin.MVNW_PROPERTIES_FIELD));

        Map<String, Object> model = new HashMap<>();
        model.put(ConstantsPlugin.NAME, dataModel.getName());

        log.info("  > [Processor] Generating README.md...");
        FreeMarkerGenerator.generate(
                ConstantsPlugin.README_SHARED,
                model,
                new File(projectDir, ConstantsPlugin.README_MD)
        );
    }

    protected void generateGitKeep(File projectDir, Log log) {
        File[] children = projectDir.listFiles();
        if (children == null) {
            return;
        }

        if (children.length == 0) {
            createGitKeep(projectDir, log);
            return;
        }

        for (File child : children) {
            if (child.isDirectory()) {
                createGitKeep(child, log);
            }
        }
    }

    private void createGitKeep(File dir, Log log) {
        File gitKeep = new File(dir, ConstantsPlugin.DOT_GITKEEP);

        if (gitKeep.exists()) {
            return;
        }

        try {
            if (gitKeep.createNewFile()) {
                log.info("  > [Processor] Created .gitkeep in " + dir.getAbsolutePath());
            }
        } catch (IOException e) {
            log.warn("  > [Processor] - [WARN] Could not create .gitkeep in " + dir.getAbsolutePath() + ": " + e.getMessage());
        }
    }

    protected void createPackages(File base, List<String> packages, Log log) {
        for (String pkg : packages) {
            log.info("  > [Processor] Creating package " + pkg);
            File dir = new File(base, pkg);
            if (!dir.mkdirs() && !dir.exists()) {
                log.warn("  > [Processor] - [WARN] Could not create directory: " + dir.getAbsolutePath());
            }
        }
    }
}
