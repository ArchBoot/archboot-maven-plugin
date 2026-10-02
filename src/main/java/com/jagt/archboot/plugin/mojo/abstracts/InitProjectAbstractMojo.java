package com.jagt.archboot.plugin.mojo.abstracts;

import com.jagt.archboot.plugin.utils.ConstantsPlugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;


/**
 * Base class holding the parameters of the {@code init} goal.
 *
 */
public abstract class InitProjectAbstractMojo extends AbstractMojo {
    // Main values
    /** Maven artifactId of the generated project. It is normalized to kebab-case. */
    @Parameter(property = ConstantsPlugin.ARTIFACT_ID, required = true)
    protected String artifactId;
    /** Maven groupId of the generated project. */
    @Parameter(property = ConstantsPlugin.GROUP_ID, required = true)
    protected String groupId;
    /** Version of the generated project. */
    @Parameter(property = ConstantsPlugin.VERSION, defaultValue = ConstantsPlugin.VERSION_DEFAULT_SNAPSHOT)
    protected String version;
    /** Short description of the generated project. */
    @Parameter(property = ConstantsPlugin.DESCRIPTION)
    protected String description;
    /** Architecture to generate. Case-insensitive. */
    @Parameter(property = ConstantsPlugin.ARCHITECTURE, required = true)
    protected String architecture;

    // Specified values
    /** Human-readable project name. Defaults to the artifactId. */
    @Parameter(property = ConstantsPlugin.NAME)
    protected String name;
    /** Base Java package. Defaults to {@code groupId + "." + artifactId}, normalized. */
    @Parameter(property = ConstantsPlugin.PACKAGE_NAME)
    protected String packageName;

    // Configuration
    /** Java version used by the generated project. */
    @Parameter(property = ConstantsPlugin.JAVA_VERSION, defaultValue = ConstantsPlugin.JAVA_VERSION_17)
    protected String javaVersion;
    /** Spring Boot version used by the generated project. */
    @Parameter(property = ConstantsPlugin.SPRING_VERSION, defaultValue = ConstantsPlugin.SPRING_VERSION_V4_0_0)
    protected String springVersion;
    /** Format of the application configuration file: {@code yml} or {@code properties}. */
    @Parameter(property = ConstantsPlugin.CONFIGURATION, defaultValue = ConstantsPlugin.YML)
    protected String configurationType;
    /** Whether to create {@code .gitkeep} files in empty directories. */
    @Parameter(property = ConstantsPlugin.GIT_KEEP, defaultValue = ConstantsPlugin.FALSE)
    protected boolean gitKeep;


    // Annotations
    /** Whether to add MapStruct to the generated project. */
    @Parameter(property = ConstantsPlugin.MAPSTRUCT, defaultValue = ConstantsPlugin.FALSE)
    protected boolean mapstruct;
    /** Whether to add Lombok to the generated project. */
    @Parameter(property = ConstantsPlugin.LOMBOK, defaultValue = ConstantsPlugin.FALSE)
    protected boolean lombok;

    // Output
    /** Directory where the project folder is created. Defaults to the current working directory. */
    @Parameter(property = ConstantsPlugin.OUTPUT)
    protected File outputDir;
}
