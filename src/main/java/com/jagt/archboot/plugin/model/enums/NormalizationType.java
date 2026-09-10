package com.jagt.archboot.plugin.model.enums;

import com.jagt.archboot.plugin.utils.Normalizer;

import java.util.function.UnaryOperator;

public enum NormalizationType {
    ENUM(Normalizer::normalizeEnum),
    ARTIFACT_ID(Normalizer::normalizeArtifactId),
    PACKAGE_NAME(Normalizer::normalizePackageName),
    CLASS_NAME(Normalizer::normalizeClassName);

    private final UnaryOperator<String> normalizer;

    NormalizationType(UnaryOperator<String> normalizer) {
        this.normalizer = normalizer;
    }

    public String apply(String raw) {
        return normalizer.apply(raw);
    }
}
