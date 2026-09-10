package com.jagt.archboot.plugin.processor;

import com.jagt.archboot.plugin.model.ScaffoldModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;

public interface ArchitectureProcessor {
    void execute(ScaffoldModel scaffoldModel, File projectDir, Log log) throws MojoExecutionException;
}
