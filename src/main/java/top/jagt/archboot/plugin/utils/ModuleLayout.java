package top.jagt.archboot.plugin.utils;

import top.jagt.archboot.plugin.model.ModuleDefinitionModel;

import java.util.List;

/**
 * Utility class that provides predefined module layouts for different architecture types.
 */
public class ModuleLayout {
    private ModuleLayout() {}

    /**
     * Returns the predefined module layout for the hexagonal architecture.
     *
     * @return the list of module definitions
     */
    public static List<ModuleDefinitionModel> hexagonal() {
        return List.of(
                ModuleDefinitionModel.builder()
                        .suffix(ConstantsPlugin.DOMAIN)
                        .description("Domain layer")
                        .dependsOn(List.of())
                        .packages(List.of(
                                ConstantsPlugin.MODEL,
                                ConstantsPlugin.GATEWAY,
                                ConstantsPlugin.EXCEPTION
                        ))
                        .generateTests(false)
                        .templatePath(ConstantsPlugin.POM_DOMAIN_HEXAGONAL)
                        .build(),
                ModuleDefinitionModel.builder()
                        .suffix(ConstantsPlugin.APPLICATION)
                        .description("Application layer")
                        .dependsOn(List.of(
                                ConstantsPlugin.DOMAIN
                        ))
                        .packages(List.of(
                                ConstantsPlugin.USECASE,
                                ConstantsPlugin.HANDLER
                        ))
                        .generateTests(true)
                        .templatePath(ConstantsPlugin.POM_APPLICATION_HEXAGONAL)
                        .build(),
                ModuleDefinitionModel.builder()
                        .suffix(ConstantsPlugin.INFRASTRUCTURE)
                        .description("Infrastructure layer")
                        .dependsOn(List.of(
                                ConstantsPlugin.APPLICATION,
                                ConstantsPlugin.DOMAIN
                        ))
                        .packages(List.of(
                                ConstantsPlugin.CONFIG,
                                ConstantsPlugin.INPUT,
                                ConstantsPlugin.OUTPUT
                        ))
                        .generateTests(true)
                        .templatePath(ConstantsPlugin.POM_INFRASTRUCTURE_HEXAGONAL)
                        .build()
        );
    }
}
