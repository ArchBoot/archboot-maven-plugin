package com.jagt.archboot.plugin.model.enums;

import com.jagt.archboot.plugin.utils.Normalizer;

/**
 * Supported formats for the generated Spring Boot application configuration file.
 */
public enum ConfigApplicationType {
    /** {@code application.yml} configuration format. */
    YML,
    /** {@code application.properties} configuration format. */
    PROPERTIES;

    /**
     * Resolve an {@link ConfigApplicationType} from a string value.
     * @param value the string value to resolve
     * @return the corresponding {@link ConfigApplicationType}, or null if not found
     */
    public static ConfigApplicationType fromString(String value) {
        if (value == null) return null;

        try {
            return ConfigApplicationType.valueOf(Normalizer.normalize(value, NormalizationType.ENUM));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
