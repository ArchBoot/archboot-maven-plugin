package com.jagt.archboot.plugin.model;

import com.jagt.archboot.plugin.model.enums.ConfigApplicationType;

public class ConfigModel {
    private ConfigApplicationType extension;
    private String javaVersion;
    private String springBootVersion;
    private boolean gitKeep;

    public ConfigApplicationType getExtension() {
        return extension;
    }

    public void setExtension(ConfigApplicationType extension) {
        this.extension = extension;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    public boolean isGitKeep() {
        return gitKeep;
    }

    public void setGitKeep(boolean gitKeep) {
        this.gitKeep = gitKeep;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ConfigModel(Builder builder) {
        this.extension = builder.extension;
        this.javaVersion = builder.javaVersion;
        this.springBootVersion = builder.springBootVersion;
        this.gitKeep = builder.gitKeep;
    }

    public static class Builder {
        private ConfigApplicationType extension;
        private String javaVersion;
        private String springBootVersion;
        private boolean gitKeep;

        public Builder extension(ConfigApplicationType extension) {
            this.extension = extension;
            return this;
        }

        public Builder javaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        public Builder springBootVersion(String springBootVersion) {
            this.springBootVersion = springBootVersion;
            return this;
        }

        public Builder gitKeep(boolean gitKeep) {
            this.gitKeep = gitKeep;
            return this;
        }

        public ConfigModel build() {
            return new ConfigModel(this);
        }
    }
}
