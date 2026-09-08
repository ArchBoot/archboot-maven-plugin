package com.jagt.archboot.plugin.processor;

import com.jagt.archboot.plugin.model.ScaffoldModel;
import org.apache.maven.plugin.MojoExecutionException;

public interface ArchitectureProcessor {
    void execute(ScaffoldModel scaffoldModel) throws MojoExecutionException;
}
