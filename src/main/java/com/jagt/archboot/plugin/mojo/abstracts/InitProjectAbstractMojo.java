package com.jagt.archboot.plugin.mojo.abstracts;

import com.jagt.archboot.plugin.utils.ConstantsPlugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class InitProjectAbstractMojo extends AbstractMojo {
    // Main values
    @Parameter(property = ConstantsPlugin.ARTIFACT_ID, required = true)
    protected String artifactId;
    @Parameter(property = ConstantsPlugin.GROUP_ID, required = true)
    protected String groupId;
    @Parameter(property = ConstantsPlugin.VERSION, defaultValue = ConstantsPlugin.VERSION_DEFAULT_SNAPSHOT)
    protected String version;
    @Parameter(property = ConstantsPlugin.DESCRIPTION)
    protected String description;
    @Parameter(property = ConstantsPlugin.ARCHITECTURE, required = true)
    protected String architecture;

    // Specified values
    @Parameter(property = ConstantsPlugin.NAME)
    protected String name;
    @Parameter(property = ConstantsPlugin.PACKAGE_NAME)
    protected String packageName;

    // Configuration
    @Parameter(property = ConstantsPlugin.JAVA_VERSION, defaultValue = ConstantsPlugin.JAVA_VERSION_17)
    protected String javaVersion;
    @Parameter(property = ConstantsPlugin.SPRING_VERSION, defaultValue = ConstantsPlugin.SPRING_VERSION_V4_0_0)
    protected String springVersion;
    @Parameter(property = ConstantsPlugin.CONFIGURATION, defaultValue = ConstantsPlugin.YML)
    protected String configurationType;
    @Parameter(property = ConstantsPlugin.GIT_KEEP, defaultValue = ConstantsPlugin.FALSE)
    protected boolean gitKeep;


    // Annotations
    @Parameter(property = ConstantsPlugin.MAPSTRUCT, defaultValue = ConstantsPlugin.FALSE)
    protected boolean mapstruct;
    @Parameter(property = ConstantsPlugin.LOMBOK, defaultValue = ConstantsPlugin.FALSE)
    protected boolean lombok;

    // Output
    @Parameter(property = ConstantsPlugin.OUTPUT)
    protected File outputDir;
}
