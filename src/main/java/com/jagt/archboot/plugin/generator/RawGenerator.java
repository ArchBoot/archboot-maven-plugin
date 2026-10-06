package com.jagt.archboot.plugin.generator;

import com.jagt.archboot.plugin.utils.ConstantsPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Utility class that copies static resources located under {@code templates/raw}.
 */
public class RawGenerator {
    private RawGenerator() {}

    /**
     * Copies a classpath resource to a file, replacing it if it exists.
     *
     * @param resourceName the resource path relative to {@code templates/raw}
     * @param destination  the output file
     * @throws IOException if the resource is not found or cannot be copied
     */
    public static void copy(String resourceName, File destination) throws IOException {
        String fullPath = ConstantsPlugin.CLASSPATH_SEPARATOR + ConstantsPlugin.TEMPLATE_RAW_PATH + ConstantsPlugin.CLASSPATH_SEPARATOR + resourceName;

        try (InputStream in = RawGenerator.class.getResourceAsStream(fullPath)) {
            if (in == null) {
                throw new IOException("[ERROR] Resource not found: " + fullPath);
            }

            if (destination.getParentFile() != null) {
                Files.createDirectories(destination.getParentFile().toPath());
            }

            Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
