package com.github.acc15.htmlres;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

@Mojo(name = "htmlres", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class HtmlResMojo extends AbstractMojo {

    @Parameter
    private File template;

    @Parameter
    private List<HtmlResourceGroup> groups;

    @Parameter
    private String minSuffix = ".min";

    @Parameter
    private boolean useMinified = false;

    public void execute() throws MojoExecutionException {

    }

}
