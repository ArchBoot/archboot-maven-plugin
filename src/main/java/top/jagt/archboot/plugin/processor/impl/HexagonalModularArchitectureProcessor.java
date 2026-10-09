package top.jagt.archboot.plugin.processor.impl;

import org.apache.maven.plugin.logging.Log;
import top.jagt.archboot.plugin.model.DataModel;
import top.jagt.archboot.plugin.model.ModuleDefinitionModel;
import top.jagt.archboot.plugin.model.ScaffoldModel;
import top.jagt.archboot.plugin.processor.abstracts.ArchitectureProcessorAbstract;
import top.jagt.archboot.plugin.utils.ConstantsPlugin;
import top.jagt.archboot.plugin.utils.ModuleLayout;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * {@link top.jagt.archboot.plugin.processor.ArchitectureProcessor} for the Hexagonal Modular architecture.
 * Creates the {@code domain}, {@code application}, and {@code infrastructure} modules.
 */
public class HexagonalModularArchitectureProcessor extends ArchitectureProcessorAbstract {
    @Override
    protected void buildStructure(ScaffoldModel config, File projectDir, Log log) throws IOException {
        log.info("  > [Processor] Getting modules definition");
        List<ModuleDefinitionModel> modules = ModuleLayout.hexagonal();

        log.info("  > [Processor] Generating pom.xml parent");
        pomGenerator.generateParent(config, projectDir, modules);

        log.info("  > [Processor] Getting modules");
        for (ModuleDefinitionModel module : modules) {
            buildModule(config, projectDir, module, log);
        }
    }

    @Override
    protected File resolveMainSourceDir(File projectDir, DataModel dataModel) throws IOException {
        return new File(projectDir,
                ConstantsPlugin.INFRASTRUCTURE + ConstantsPlugin.CLASSPATH_SEPARATOR +
                        ConstantsPlugin.SRC_MAIN_JAVA + dataModel.getPackagePath());
    }

    @Override
    protected File resolveResourcesDir(File projectDir, DataModel dataModel) throws IOException {
        return new File(projectDir, ConstantsPlugin.INFRASTRUCTURE + ConstantsPlugin.CLASSPATH_SEPARATOR +
                ConstantsPlugin.SRC_MAIN_RESOURCES);
    }

    @Override
    protected String variantLabel() {
        return "hexagonal::modular";
    }

    private void buildModule(ScaffoldModel scaffold, File projectDir, ModuleDefinitionModel module, Log log) throws IOException {
        DataModel data = scaffold.getData();
        String moduleName = module.getSuffix();

        log.info("  > [Processor] Generating " + moduleName + " module");
        File moduleDir = new File(projectDir, moduleName);

        log.info("  > [Processor] Generating pom.xml for " + moduleName + " module");
        pomGenerator.generateModulePom(scaffold, moduleDir, module.getTemplatePath());

        String packagePath = data.getPackagePath() + ConstantsPlugin.CLASSPATH_SEPARATOR + moduleName;

        File srcMain = new File(moduleDir, ConstantsPlugin.SRC_MAIN_JAVA + packagePath);
        createPackages(srcMain, module.getPackages(), log);

        if (module.isGenerateTests()) {
            File srcTest = new File(
                    moduleDir,
                    ConstantsPlugin.SRC_TEST_JAVA + packagePath
            );

            if (!srcTest.mkdirs() && !srcTest.exists()) {
                log.warn("  > [Processor] Could not create test directory: " + srcTest.getAbsolutePath());
            }
        }
    }
}
