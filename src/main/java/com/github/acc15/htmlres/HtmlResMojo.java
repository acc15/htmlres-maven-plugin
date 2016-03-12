package com.github.acc15.htmlres;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Mojo(name = "htmlres", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class HtmlResMojo extends AbstractMojo implements ResourceGroup {

    @Parameter
    private List<HtmlResourceGroup> groups;

    @Parameter
    private File template;

    @Parameter
    private File targetFile;

    @Parameter
    private String minSuffix = ".min";

    @Parameter
    private Boolean useMinified = false;

    @Parameter
    private String urlPrefix;

    @Parameter
    private String templateEncoding = "UTF-8";

    @Parameter
    private String targetEncoding = "UTF-8";

    @Parameter
    private List<HtmlResource> jsResources;

    @Parameter
    private List<HtmlResource> cssResources;


    public void execute() throws MojoExecutionException {
        processGroup(this);
        if (groups != null) {
            final HtmlResourceGroupMergeHelper groupMergeHelper = new HtmlResourceGroupMergeHelper(this);
            groupMergeHelper.mergeGroups(groups);
            groups.forEach(HtmlResMojo::processGroup);
        }
    }

    private static void processGroup(ResourceGroup group) {
        if (group.getTargetFile() == null) {
            // skipping groups without target
            return;
        }
        if (group.getTemplate() == null) {
            throw new IllegalArgumentException("missing template file");
        }

        final HtmlTemplateProcessor templateProcessor = new HtmlTemplateProcessor();
        try {
            templateProcessor.substituteUrls(group);
        } catch (IOException e) {
            throw new RuntimeException("Can't render template \'" + group.getTemplate() + "\" to \"" + group.getTargetFile() + "\"", e);
        }
    }

    @Override
    public String getTemplateEncoding() {
        return templateEncoding;
    }

    @Override
    public String getTargetEncoding() {
        return targetEncoding;
    }

    @Override
    public File getTargetFile() {
        return targetFile;
    }

    @Override
    public File getTemplate() {
        return template;
    }

    @Override
    public String getMinSuffix() {
        return minSuffix;
    }

    @Override
    public Boolean getUseMinified() {
        return useMinified;
    }

    @Override
    public List<HtmlResource> getJsResources() {
        return jsResources;
    }

    @Override
    public List<HtmlResource> getCssResources() {
        return cssResources;
    }

    @Override
    public String getUrlPrefix() {
        return urlPrefix;
    }
}
