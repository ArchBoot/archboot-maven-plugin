package com.jagt.archboot.plugin.model;

public class AnnotationModel {
    private boolean mapstruct;
    private boolean lombok;

    public boolean isMapstruct() {
        return mapstruct;
    }

    public void setMapstruct(boolean mapstruct) {
        this.mapstruct = mapstruct;
    }

    public boolean isLombok() {
        return lombok;
    }

    public void setLombok(boolean lombok) {
        this.lombok = lombok;
    }

    public AnnotationModel(Builder builder) {
        this.mapstruct = builder.mapstruct;
        this.lombok = builder.lombok;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean mapstruct;
        private boolean lombok;

        public Builder mapstruct(boolean mapstruct) {
            this.mapstruct = mapstruct;
            return this;
        }

        public Builder lombok(boolean lombok) {
            this.lombok = lombok;
            return this;
        }

        public AnnotationModel build() {
            return new AnnotationModel(this);
        }
    }
}
