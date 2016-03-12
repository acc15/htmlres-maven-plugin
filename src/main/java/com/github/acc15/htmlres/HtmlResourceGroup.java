package com.github.acc15.htmlres;

import java.io.File;
import java.util.List;

public class HtmlResourceGroup implements ResourceGroup {

    private String id;
    private String parent;
    private Boolean useMinified;
    private String minSuffix;
    private File template;
    private File targetFile;
    private String urlPrefix;
    private String templateEncoding;
    private String targetEncoding;
    private List<HtmlResource> jsResources;
    private List<HtmlResource> cssResources;

    public void merge(ResourceGroup parent) {
        if (useMinified == null) {
            useMinified = parent.getUseMinified();
        }
        if (urlPrefix == null) {
            urlPrefix = parent.getUrlPrefix();
        }
        if (minSuffix == null) {
            minSuffix = parent.getMinSuffix();
        }

        if (targetFile == null) {
            targetFile = parent.getTargetFile();
            targetEncoding = parent.getTargetEncoding();
        }
        if (targetEncoding == null) {
            targetEncoding = parent.getTargetEncoding();
        }
        if (template == null) {
            template = parent.getTemplate();
            templateEncoding = parent.getTemplateEncoding();
        }
        if (templateEncoding == null) {
            parent.getTemplateEncoding();
        }

        if (jsResources == null) {
            jsResources = parent.getJsResources();
        } else if (parent.getJsResources() != null) {
            jsResources.addAll(0, parent.getJsResources());
        }
        if (cssResources == null) {
            cssResources = parent.getCssResources();
        } else if (parent.getCssResources() != null) {
            cssResources.addAll(0, parent.getCssResources());
        }
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public Boolean getUseMinified() {
        return useMinified;
    }

    public void setUseMinified(Boolean useMinified) {
        this.useMinified = useMinified;
    }

    @Override
    public String getMinSuffix() {
        return minSuffix;
    }

    public void setMinSuffix(String minSuffix) {
        this.minSuffix = minSuffix;
    }

    @Override
    public File getTemplate() {
        return template;
    }

    public void setTemplate(File template) {
        this.template = template;
    }

    @Override
    public String getTemplateEncoding() {
        return templateEncoding;
    }

    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    @Override
    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    @Override
    public String getTargetEncoding() {
        return targetEncoding;
    }

    public void setTargetEncoding(String targetEncoding) {
        this.targetEncoding = targetEncoding;
    }

    @Override
    public List<HtmlResource> getJsResources() {
        return jsResources;
    }

    public void setJsResources(List<HtmlResource> jsResources) {
        this.jsResources = jsResources;
    }

    @Override
    public List<HtmlResource> getCssResources() {
        return cssResources;
    }

    public void setCssResources(List<HtmlResource> cssResources) {
        this.cssResources = cssResources;
    }
}
