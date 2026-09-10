package com.jagt.archboot.plugin.utils;

import com.jagt.archboot.plugin.model.AnnotationModel;
import com.jagt.archboot.plugin.model.ConfigModel;
import com.jagt.archboot.plugin.model.DataModel;
import com.jagt.archboot.plugin.model.ScaffoldModel;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlReader {
    private YamlReader() {}

    public static void write(ScaffoldModel scaffoldModel, File projectDir) throws IOException {
        File scaffoldFile = new File(projectDir, ConstantsPlugin.SCAFFOLD_FILE);

        Yaml yaml = new Yaml(getOptions());

        Map<String, Object> root = buildYaml(scaffoldModel);

        Path parentPath = scaffoldFile.toPath().getParent();
        if (parentPath != null) {
            Files.createDirectories(parentPath);
        }

        try (Writer writer = new FileWriter(scaffoldFile)) {
            yaml.dump(root, writer);
        }
    }

    private static DumperOptions getOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);
        return options;
    }

    private static Map<String, Object> buildYaml(ScaffoldModel scaffoldModel) {
        return map(
                ConstantsPlugin.SCAFFOLD, map(
                        ConstantsPlugin.ARCHITECTURE, scaffoldModel.getArchitecture().name(),
                        ConstantsPlugin.DATA, buildData(scaffoldModel.getData())
                )
        );
    }

    private static Map<String, Object> buildData(DataModel dataModel) {
        return map(
                ConstantsPlugin.ARTIFACT_ID, dataModel.getArtifactId(),
                ConstantsPlugin.GROUP_ID, dataModel.getGroupId(),
                ConstantsPlugin.NAME, dataModel.getName(),
                ConstantsPlugin.PACKAGE, dataModel.getPackageName(),
                ConstantsPlugin.ANNOTATIONS, buildAnnotations(dataModel.getAnnotation()),
                ConstantsPlugin.CONFIG, buildConfig(dataModel.getConfig())
        );
    }

    private static Map<String, Object> buildAnnotations(AnnotationModel annotationModel) {
        if (annotationModel == null) {
            return map();
        }
        return map(
                ConstantsPlugin.LOMBOK, annotationModel.isLombok(),
                ConstantsPlugin.MAPSTRUCT, annotationModel.isMapstruct()
        );
    }

    private static Map<String, Object> buildConfig(ConfigModel configModel) {
        if (configModel == null) {
            return map();
        }
        return map(
                ConstantsPlugin.CONFIG, map(
                        ConstantsPlugin.EXTENSION, configModel.getExtension()
                )
        );
    }

    private static Map<String, Object> map(Object... keyValues) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put((String) keyValues[i], keyValues[i + 1]);
        }
        return map;
    }
}
