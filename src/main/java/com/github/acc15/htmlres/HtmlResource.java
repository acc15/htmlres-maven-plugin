package com.github.acc15.htmlres;

import java.io.File;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-08-02
 */
public class HtmlResource {

    private String urlPrefix;
    private String minSuffix;
    private Boolean useMinified;

    private String url;
    private File dir;
    private String[] includes;
    private String[] excludes;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getMinSuffix() {
        return minSuffix;
    }

    public void setMinSuffix(String minSuffix) {
        this.minSuffix = minSuffix;
    }

    public Boolean getUseMinified() {
        return useMinified;
    }

    public void setUseMinified(Boolean useMinified) {
        this.useMinified = useMinified;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public String[] getExcludes() {
        return excludes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }
}
