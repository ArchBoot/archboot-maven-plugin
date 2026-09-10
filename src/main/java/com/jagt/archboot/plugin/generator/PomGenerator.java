package com.jagt.archboot.plugin.generator;

import com.jagt.archboot.plugin.model.DataModel;
import com.jagt.archboot.plugin.model.ScaffoldModel;
import com.jagt.archboot.plugin.utils.ConstantsPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PomGenerator {
    public void generateStandard(ScaffoldModel scaffold, File projectDir) throws IOException {
        FreeMarkerGenerator.generate(
                ConstantsPlugin.POM_SHARED,
                buildBaseModel(scaffold.getData()),
                new File(projectDir, ConstantsPlugin.POM_XML)
        );
    }

    private Map<String, Object> buildBaseModel(DataModel dataModel) {
        Map<String, Object> model = new HashMap<>();

        if (dataModel == null) {
            return model;
        }

        model.put(ConstantsPlugin.ARTIFACT_ID, dataModel.getArtifactId());
        model.put(ConstantsPlugin.GROUP_ID, dataModel.getGroupId());
        model.put(ConstantsPlugin.VERSION, dataModel.getVersion());
        model.put(ConstantsPlugin.NAME, dataModel.getName());
        model.put(ConstantsPlugin.DESCRIPTION, dataModel.getDescription());

        if (dataModel.getAnnotation() != null) {
            model.put(ConstantsPlugin.LOMBOK, dataModel.getAnnotation().isLombok());
            model.put(ConstantsPlugin.MAPSTRUCT, dataModel.getAnnotation().isMapstruct());
        }

        if (dataModel.getConfig() != null) {
            model.put(ConstantsPlugin.EXTENSION, dataModel.getConfig().getExtension());
            model.put(ConstantsPlugin.JAVA_VERSION, dataModel.getConfig().getJavaVersion());
            model.put(ConstantsPlugin.SPRING_VERSION, dataModel.getConfig().getSpringBootVersion());
        }

        return model;
    }
}
