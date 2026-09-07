package com.jagt.archboot.plugin.model.enums;

import com.jagt.archboot.plugin.utils.Normalizer;

public enum ConfigApplicationType {
    YML,
    PROPERTIES;

    public static ConfigApplicationType fromString(String value) {
        if (value == null) return null;

        try {
            return ConfigApplicationType.valueOf(Normalizer.normalize(value, NormalizationType.ENUM));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
