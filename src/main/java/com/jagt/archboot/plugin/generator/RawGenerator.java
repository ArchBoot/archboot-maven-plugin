package com.jagt.archboot.plugin.generator;

import com.jagt.archboot.plugin.utils.ConstantsPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class RawGenerator {
    private RawGenerator() {}

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
