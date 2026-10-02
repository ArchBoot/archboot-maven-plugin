package com.jagt.archboot.plugin.model.enums;

import com.jagt.archboot.plugin.utils.Normalizer;

import java.util.function.UnaryOperator;

/**
 * Normalization strategies available to {@link Normalizer}.
 */
public enum NormalizationType {
    /** Enum constant name normalization. */
    ENUM(Normalizer::normalizeEnum),
    /** Maven artifact id normalization. */
    ARTIFACT_ID(Normalizer::normalizeArtifactId),
    /** Java package name normalization. */
    PACKAGE_NAME(Normalizer::normalizePackageName),
    /** Java class name normalization. */
    CLASS_NAME(Normalizer::normalizeClassName);

    private final UnaryOperator<String> normalizer;

    NormalizationType(UnaryOperator<String> normalizer) {
        this.normalizer = normalizer;
    }

    /**
     * Applies this normalization to a value.
     *
     * @param raw the raw value
     * @return the normalized value
     */
    public String apply(String raw) {
        return normalizer.apply(raw);
    }
}
