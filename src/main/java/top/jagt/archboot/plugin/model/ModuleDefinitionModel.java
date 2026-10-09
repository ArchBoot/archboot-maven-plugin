package top.jagt.archboot.plugin.model;

import java.util.List;

public class ModuleDefinitionModel {
    private final String suffix;
    private final String description;
    private final List<String> dependsOn;
    private final List<String> packages;
    private final boolean generateTests;
    private final String templatePath;

    private ModuleDefinitionModel(Builder builder) {
        this.suffix = builder.suffix;
        this.description = builder.description;
        this.dependsOn = builder.dependsOn;
        this.packages = builder.packages;
        this.generateTests = builder.generateTests;
        this.templatePath = builder.templatePath;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getDependsOn() {
        return dependsOn;
    }

    public List<String> getPackages() {
        return packages;
    }

    public boolean isGenerateTests() {
        return generateTests;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String suffix = "";
        private String description = "";
        private List<String> dependsOn = List.of();
        private List<String> packages = List.of();
        private boolean generateTests = false;
        private String templatePath = "";

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder dependsOn(List<String> dependsOn) {
            this.dependsOn = dependsOn;
            return this;
        }

        public Builder packages(List<String> packages) {
            this.packages = packages;
            return this;
        }

        public Builder generateTests(boolean generateTests) {
            this.generateTests = generateTests;
            return this;
        }

        public Builder templatePath(String templatePath) {
            this.templatePath = templatePath;
            return this;
        }

        public ModuleDefinitionModel build() {
            return new ModuleDefinitionModel(this);
        }
    }
}
