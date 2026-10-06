package com.jagt.archboot.plugin.model;

import com.jagt.archboot.plugin.utils.ConstantsPlugin;

public class DataModel {
    private String artifactId;
    private String groupId;
    private String version;
    private String name;
    private String description;
    private String packageName;
    private AnnotationModel annotation;
    private ConfigModel config;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackagePath() {
        return packageName.replace(".", ConstantsPlugin.CLASSPATH_SEPARATOR);
    }

    public AnnotationModel getAnnotation() {
        return annotation;
    }

    public void setAnnotation(AnnotationModel annotation) {
        this.annotation = annotation;
    }

    public ConfigModel getConfig() {
        return config;
    }

    public void setConfig(ConfigModel config) {
        this.config = config;
    }

    public DataModel(Builder builder) {
        this.artifactId = builder.artifactId;
        this.groupId = builder.groupId;
        this.version = builder.version;
        this.name = builder.name;
        this.description = builder.description;
        this.packageName = builder.packageName;
        this.annotation = builder.annotation;
        this.config = builder.config;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String artifactId;
        private String groupId;
        private String version;
        private String name;
        private String description;
        private String packageName;
        private AnnotationModel annotation;
        private ConfigModel config;

        public Builder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder annotation(AnnotationModel annotation) {
            this.annotation = annotation;
            return this;
        }

        public Builder config(ConfigModel config) {
            this.config = config;
            return this;
        }

        public DataModel build() {
            return new DataModel(this);
        }
    }
}
