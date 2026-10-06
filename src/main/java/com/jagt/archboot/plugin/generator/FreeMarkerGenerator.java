package com.jagt.archboot.plugin.generator;

import com.jagt.archboot.plugin.utils.ConstantsPlugin;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class that renders FreeMarker templates located under {@code templates/ftl}.
 */
public class FreeMarkerGenerator {
    private FreeMarkerGenerator() {}

    /**
     * Global FreeMarker configuration used to load and process FTL templates.
     */
    private static final Configuration FTL_CONFIG;

    static {
        FTL_CONFIG = new Configuration(Configuration.VERSION_2_3_33);
        FTL_CONFIG.setClassLoaderForTemplateLoading(
                FreeMarkerGenerator.class.getClassLoader(), ConstantsPlugin.TEMPLATE_FTL_PATH
        );
        FTL_CONFIG.setDefaultEncoding(StandardCharsets.UTF_8.name());
        FTL_CONFIG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        FTL_CONFIG.setLogTemplateExceptions(false);
    }

    /**
     * Renders a template into a file, creating parent directories when needed.
     * If the model contains an {@code imports} list of strings, it is sorted before rendering.
     *
     * @param templateName the template path relative to {@code templates/ftl}
     * @param model        the data model passed to the template
     * @param destination  the output file
     * @throws IOException if the template is missing or invalid, or the file cannot be written
     */
    public static void generate(String templateName, Map<String, Object> model, File destination) throws IOException {
        try {
            Template template = FTL_CONFIG.getTemplate(templateName);

            if (destination.getParentFile() != null) {
                Files.createDirectories(destination.getParentFile().toPath());
            }

            sortImportsIfPresent(model);

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(destination), StandardCharsets.UTF_8))) {
                template.process(model, writer);
            }
        } catch (TemplateNotFoundException | ParseException | MalformedTemplateNameException | TemplateException e) {
            throw new IOException("[ERROR] Template error [" + templateName + "]: " + e.getMessage(), e);
        }

    }

    private static void sortImportsIfPresent(Map<String, Object> model) {
        Object raw = model.get(ConstantsPlugin.IMPORTS);
        if (raw instanceof List<?> list && list.stream().allMatch(String.class::isInstance)) {
            List<String> sorted = list.stream()
                            .map(String.class::cast)
                            .sorted()
                            .collect(Collectors.toCollection(ArrayList::new));
            model.put(ConstantsPlugin.IMPORTS, sorted);
        }
    }
}
