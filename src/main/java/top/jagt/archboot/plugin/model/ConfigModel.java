package top.jagt.archboot.plugin.model;

import top.jagt.archboot.plugin.model.enums.ConfigApplicationType;

import java.util.List;

public class ConfigModel {
    private ConfigApplicationType extension;
    private String javaVersion;
    private String springBootVersion;
    private boolean gitKeep;
    private List<String> services;
    private boolean configServer;

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

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public boolean isConfigServer() {
        return configServer;
    }

    public void setConfigServer(boolean configServer) {
        this.configServer = configServer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ConfigModel(Builder builder) {
        this.extension = builder.extension;
        this.javaVersion = builder.javaVersion;
        this.springBootVersion = builder.springBootVersion;
        this.gitKeep = builder.gitKeep;
        this.services = builder.services;
        this.configServer = builder.configServer;
    }

    public static class Builder {
        private ConfigApplicationType extension;
        private String javaVersion;
        private String springBootVersion;
        private boolean gitKeep;
        private List<String> services;
        private boolean configServer;

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

        public Builder services(List<String> services) {
            this.services = services;
            return this;
        }

        public Builder configServer(boolean configServer) {
            this.configServer = configServer;
            return this;
        }

        public ConfigModel build() {
            return new ConfigModel(this);
        }
    }
}
