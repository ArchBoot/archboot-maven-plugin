package top.jagt.archboot.plugin.processor.abstracts;

import top.jagt.archboot.plugin.generator.FreeMarkerGenerator;
import top.jagt.archboot.plugin.generator.PomGenerator;
import top.jagt.archboot.plugin.generator.RawGenerator;
import top.jagt.archboot.plugin.model.DataModel;
import top.jagt.archboot.plugin.model.ScaffoldModel;
import top.jagt.archboot.plugin.model.enums.NormalizationType;
import top.jagt.archboot.plugin.processor.ArchitectureProcessor;
import top.jagt.archboot.plugin.utils.ConstantsPlugin;
import top.jagt.archboot.plugin.utils.Normalizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Template-method base for {@link ArchitectureProcessor} implementations.
 *
 * <p>It runs the common generation flow (structure, main class, resources, base files and
 * optional {@code .gitkeep} files). Subclasses only provide the architecture-specific parts.</p>
 *
 */
public abstract class ArchitectureProcessorAbstract implements ArchitectureProcessor {
    /** Generator used to produce the {@code pom.xml}. */
    protected final PomGenerator pomGenerator = new PomGenerator();

    /** {@inheritDoc} */
    @Override
    public void execute(ScaffoldModel scaffoldModel, File projectDir, Log log) throws MojoExecutionException {
        try {
            DataModel dataModel = scaffoldModel.getData();

            log.info("> [Processor] Building architecture structure for " + variantLabel() + " variant");
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

    /**
     * Creates the architecture-specific structure (pom and packages).
     *
     * @param config     the scaffold model
     * @param projectDir the project directory
     * @param log        the Maven logger
     * @throws IOException if a file or directory cannot be created
     */
    protected abstract void buildStructure(ScaffoldModel config, File projectDir, Log log) throws IOException;

    /**
     * Resolves the directory where the main application class is generated.
     *
     * @param projectDir the project directory
     * @param dataModel  the project data
     * @return the main source directory
     * @throws IOException if the directory cannot be resolved
     */
    protected abstract File resolveMainSourceDir(File projectDir, DataModel dataModel) throws IOException;

    /**
     * Resolves the directory where the configuration file is generated.
     *
     * @param projectDir the project directory
     * @param dataModel  the project data
     * @return the resources directory
     * @throws IOException if the directory cannot be resolved
     */
    protected abstract File resolveResourcesDir(File projectDir, DataModel dataModel) throws IOException;

    /**
     * Returns a short label identifying the architecture variant, used in log messages.
     *
     * @return the variant label
     */
    protected abstract String variantLabel();

    /**
     * Generates the Spring Boot main class from the {@code App.java} template.
     *
     * @param dataModel the project data
     * @param srcDir    the directory where the class is written
     * @throws IOException if the template cannot be processed
     */
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

    /**
     * Generates the application configuration resource file ({@code application.yml}
     * or {@code application.properties}, depending on {@link DataModel}'s config extension).
     *
     * @param dataModel    the project data model
     * @param resourcesDir the directory where the resource file will be written
     * @throws IOException if the template could not be processed or the file written
     */
    protected void generateResources(DataModel dataModel, File resourcesDir) throws IOException {

        String configFile = ConstantsPlugin.YML.equalsIgnoreCase(dataModel.getConfig().getExtension().name())
                ? ConstantsPlugin.APPLICATION_YML : ConstantsPlugin.APPLICATION_PROPERTIES;

        Map<String, Object> model = new HashMap<>();
        model.put(ConstantsPlugin.NAME, dataModel.getName());

        FreeMarkerGenerator.generate(
                ConstantsPlugin.SHARED + ConstantsPlugin.CLASSPATH_SEPARATOR + configFile + ConstantsPlugin.DOT_FTL,
                model,
                new File(resourcesDir, configFile)
        );
    }

    /**
     * Generates the base files: {@code .gitignore}, {@code .gitattributes}, Maven wrapper
     * scripts and properties, and {@code README.md}.
     *
     * @param projectDir the project directory
     * @param dataModel  the project data
     * @param log        the Maven logger
     * @throws IOException if a file cannot be generated
     */
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

    /**
     * Creates a {@code .gitkeep} file in every empty leaf directory under the given one.
     *
     * @param projectDir the root directory to scan
     * @param log        the Maven logger
     */
    protected void generateGitKeep(File projectDir, Log log) {
        generateGitKeepRecursive(projectDir, log);
    }

    private void generateGitKeepRecursive(File directory, Log log) {
        File[] children = directory.listFiles();

        if (children == null) {
            return;
        }

        boolean hasRealContent = false;

        for (File child : children) {
            if (child.isDirectory()) {
                generateGitKeepRecursive(child, log);
            } else if (!child.getName().equals(ConstantsPlugin.DOT_GITKEEP)) {
                hasRealContent = true;
            }
        }

        boolean hasDirectories = false;

        for (File child : children) {
            if (child.isDirectory()) {
                hasDirectories = true;
                break;
            }
        }

        if (!hasRealContent && !hasDirectories) {
            createGitKeep(directory, log);
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

    /**
     * Creates the given packages (directories) under a base directory.
     *
     * @param base     the base directory
     * @param packages the package names to create
     * @param log      the Maven logger
     */
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
