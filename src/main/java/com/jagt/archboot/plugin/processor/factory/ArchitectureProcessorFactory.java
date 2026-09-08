package com.jagt.archboot.plugin.processor.factory;

import com.jagt.archboot.plugin.model.enums.ArchitectureType;
import com.jagt.archboot.plugin.processor.ArchitectureProcessor;
import com.jagt.archboot.plugin.processor.impl.MvcArchitectureProcessor;

import java.util.HashMap;
import java.util.Map;

public class ArchitectureProcessorFactory {
    private ArchitectureProcessorFactory() {}

    private static final Map<ArchitectureType, ArchitectureProcessor> PROCESSORS = new HashMap<>();

    static {
        PROCESSORS.put(ArchitectureType.MVC, new MvcArchitectureProcessor());
    }

    public static ArchitectureProcessor get(ArchitectureType type) {
        ArchitectureProcessor processor = PROCESSORS.get(type);

        if (processor == null) {
            throw new IllegalArgumentException("[ERR0R] Unknown architecture type: " + type);
        }
        return processor;
    }
}
