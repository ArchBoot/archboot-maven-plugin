package com.jagt.archboot.plugin.utils;

import com.jagt.archboot.plugin.model.enums.NormalizationType;

import java.util.Set;
import java.util.regex.Pattern;

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

    public static String normalize(String raw, NormalizationType type) {
        return type.apply(raw);
    }

    public static String normalizeEnum(String raw) {
        return ENUM_SEPARATOR.matcher(raw.toUpperCase()).replaceAll("_");
    }

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
