package com.jagt.archboot.plugin.model;

import com.jagt.archboot.plugin.model.enums.ArchitectureType;

public class ScaffoldModel {
    private ArchitectureType architecture;
    private DataModel data;

    public ArchitectureType getArchitecture() {
        return architecture;
    }

    public void setArchitecture(ArchitectureType architecture) {
        this.architecture = architecture;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public ScaffoldModel(Builder builder) {
        this.architecture = builder.architecture;
        this.data = builder.data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ArchitectureType architecture;
        private DataModel data;

        public Builder architecture(ArchitectureType architecture) {
            this.architecture = architecture;
            return this;
        }

        public Builder data(DataModel data) {
            this.data = data;
            return this;
        }

        public ScaffoldModel build() {
            return new ScaffoldModel(this);
        }
    }
}
