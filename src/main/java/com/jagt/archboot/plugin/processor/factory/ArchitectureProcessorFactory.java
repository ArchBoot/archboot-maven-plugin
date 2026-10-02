package com.jagt.archboot.plugin.processor.factory;

import com.jagt.archboot.plugin.model.enums.ArchitectureType;
import com.jagt.archboot.plugin.processor.ArchitectureProcessor;
import com.jagt.archboot.plugin.processor.impl.MvcArchitectureProcessor;

import java.util.EnumMap;
import java.util.Map;

/**
 * Registry that resolves the {@link ArchitectureProcessor} for an {@link ArchitectureType}.
 */
public class ArchitectureProcessorFactory {
    private ArchitectureProcessorFactory() {}

    private static final Map<ArchitectureType, ArchitectureProcessor> PROCESSORS = new EnumMap<>(ArchitectureType.class);

    static {
        PROCESSORS.put(ArchitectureType.MVC, new MvcArchitectureProcessor());
    }

    /**
     * Returns the {@link ArchitectureProcessor} registered for the given architecture type.
     *
     * @param type the architecture type to resolve
     * @return the processor implementation for {@code type}
     * @throws IllegalArgumentException if no processor is registered for {@code type}
     */
    public static ArchitectureProcessor get(ArchitectureType type) {
        ArchitectureProcessor processor = PROCESSORS.get(type);

        if (processor == null) {
            throw new IllegalArgumentException("[ERR0R] Unknown architecture type: " + type);
        }
        return processor;
    }
}
