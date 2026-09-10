package com.jagt.archboot.plugin.processor.impl;

import com.jagt.archboot.plugin.model.DataModel;
import com.jagt.archboot.plugin.model.ScaffoldModel;
import com.jagt.archboot.plugin.processor.abstracts.ArchitectureProcessorAbstract;
import com.jagt.archboot.plugin.utils.ConstantsPlugin;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MvcArchitectureProcessor extends ArchitectureProcessorAbstract {

    @Override
    protected void buildStructure(ScaffoldModel config, File projectDir, Log log) throws IOException {
        log.info("  > [Processor::MVC] Generating pom.xml");
        pomGenerator.generateStandard(config, projectDir);

        DataModel data = config.getData();

        String pkg = data.getPackagePath();

        File src = new File(projectDir, ConstantsPlugin.SRC_MAIN_JAVA + pkg);
        createPackages(src, List.of(
                ConstantsPlugin.CONTROLLER,
                ConstantsPlugin.SERVICE,
                ConstantsPlugin.REPOSITORY,
                ConstantsPlugin.MODEL
        ), log);

        new File(projectDir, ConstantsPlugin.SRC_TEST_JAVA).mkdirs();
    }

    @Override
    protected File resolveMainSourceDir(File projectDir, DataModel dataModel) throws IOException {
        return new File(projectDir, ConstantsPlugin.SRC_MAIN_JAVA + dataModel.getPackagePath());
    }

    @Override
    protected File resolveResourcesDir(File projectDir, DataModel dataModel) throws IOException {
        return new File(projectDir, ConstantsPlugin.SRC_MAIN_RESOURCES);
    }

    @Override
    protected String variantLabel() {
        return "mvc::mono";
    }
}
