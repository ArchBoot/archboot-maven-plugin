package com.jagt.archboot.plugin.processor;

import com.jagt.archboot.plugin.model.ScaffoldModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;

/**
 * Strategy that builds the project structure for a given architecture.
 */
public interface ArchitectureProcessor {
    /**
     * Generates the project files and directories.
     *
     * @param scaffoldModel the scaffold model describing the project
     * @param projectDir    the already created project directory
     * @param log           the Maven logger
     * @throws MojoExecutionException if the generation fails
     */
    void execute(ScaffoldModel scaffoldModel, File projectDir, Log log) throws MojoExecutionException;
}
