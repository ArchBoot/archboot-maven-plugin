package com.jagt.archboot.plugin.utils;

import com.jagt.archboot.plugin.model.enums.NormalizationType;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * String normalization helpers for enum names, artifact ids, package names and class names.
 */
public class Normalizer {
    private Normalizer() {}

    private static final Set<String> JAVA_KEYWORDS = Set.of(
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    );
    private static final Pattern ENUM_SEPARATOR = Pattern.compile("[-\\s]+");

    /**
     * Normalizes a value with the strategy of the given type.
     *
     * @param raw  the raw value
     * @param type the normalization strategy
     * @return the normalized value
     */
    public static String normalize(String raw, NormalizationType type) {
        return type.apply(raw);
    }

    /**
     * Converts a value to an enum constant name: upper case, with hyphens and
     * whitespace replaced by underscores.
     *
     * @param raw the raw value
     * @return the enum-style name
     */
    public static String normalizeEnum(String raw) {
        return ENUM_SEPARATOR.matcher(raw.toUpperCase()).replaceAll("_");
    }

    /**
     * Converts a value to a kebab-case artifact id (for example {@code MyApp} becomes
     * {@code my-app}). Returns {@code "app"} when the result is blank.
     *
     * @param raw the raw value
     * @return the normalized artifact id
     */
    public static String normalizeArtifactId(String raw) {
        String value = raw.replaceAll("([a-z])([A-Z])", "$1-$2") // CamelCase -> kebab-case
                .toLowerCase()
                .replaceAll("[_/]", "-")
                .replace(".", "-")
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");

        return value.isBlank() ? "app" : value;
    }

    /**
     * Converts a value to a valid lower-case Java package name. Hyphens become underscores,
     * Java keywords get a trailing underscore and segments starting with a digit get a
     * leading underscore.
     *
     * @param raw the raw value
     * @return the normalized package name
     */
    public static String normalizePackageName(String raw) {
        String value = raw.toLowerCase()
                .replace(ConstantsPlugin.CLASSPATH_SEPARATOR, ".")
                .replace("-", "_")
                .replaceAll("[^a-z0-9._]", ".")
                .replaceAll("\\.+", ".")
                .replaceAll("^\\.|\\.$", "");

        String[] parts = value.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) continue;

            if (JAVA_KEYWORDS.contains(parts[i])) {
                parts[i] = parts[i] + "_";
            }

            if (Character.isDigit(parts[i].charAt(0))) {
                parts[i] = "_" + parts[i];
            }
        }

        return String.join(".", parts);
    }

    /**
     * Converts a value to a PascalCase class name ending in {@code Application}.
     * A {@code App} prefix is added when the name is empty or starts with a digit.
     *
     * @param raw the raw value
     * @return the normalized class name
     */ 
    public static String normalizeClassName(String raw) {
        String cleaned = raw.replaceAll("[^a-zA-Z0-9]", " ").trim();

        StringBuilder className = new StringBuilder();

        for (String part : cleaned.split("\\s+")) {
            if (part.isBlank()) continue;

            className
                    .append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase());
        }

        if (className.isEmpty() || Character.isDigit(className.charAt(0))) {
            className.insert(0, "App");
        }

        String result = className.toString();

        return result + "Application";
    }
}
