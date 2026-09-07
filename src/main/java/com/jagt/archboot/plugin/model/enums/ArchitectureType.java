package com.jagt.archboot.plugin.model.enums;

import com.jagt.archboot.plugin.utils.Normalizer;

public enum ArchitectureType {
    MVC;

    public static ArchitectureType fromString(String value) {
        if (value == null) return null;

        try {
            return ArchitectureType.valueOf(Normalizer.normalize(value, NormalizationType.ENUM));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
