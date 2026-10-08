package top.jagt.archboot.plugin.model.enums;

import top.jagt.archboot.plugin.utils.Normalizer;

/**
 * Supported architecture types for the scaffold generator.
 */
public enum ArchitectureType {
    /** Traditional Model-View-Controller layered architecture. */
    MVC,
    /** Hexagonal with modules layered architecture **/
    HEXAGONAL_MODULAR;

    /**
     * Resolve an {@link ArchitectureType} from a string value.
     * @param value the string value to resolve
     * @return the corresponding {@link ArchitectureType}, or null if not found
     */
    public static ArchitectureType fromString(String value) {
        if (value == null) return null;

        try {
            return ArchitectureType.valueOf(Normalizer.normalize(value, NormalizationType.ENUM));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
