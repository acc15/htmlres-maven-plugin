package com.github.acc15.htmlres;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public void extractUrls(Collection<String> urls, ResourceGroup group) {
        if (url != null) {
            urls.add(getTargetUrl(url, group));
            return;
        }

        if (dir != null) {
            final DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(dir);
            if (includes != null) {
                scanner.setIncludes(includes);
            }
            if (excludes != null) {
                scanner.setExcludes(excludes);
            }
            scanner.scan();

            urls.addAll(Arrays.asList(scanner.getIncludedFiles()).stream().
                    map(s -> getTargetUrl(s.replace('\\', '/'), group)).
                    collect(Collectors.toList()));
        }
    }

    private String getTargetUrl(String url, ResourceGroup group) {
        final boolean useMinified = getUseMinified(group);
        if (useMinified) {
            final String minSuffix = getMinSuffix(group);
            if (minSuffix != null) {
                final int extPos = url.lastIndexOf('.');
                if (extPos < 0) {
                    url = url + minSuffix;
                } else {
                    url = url.substring(0, extPos) + minSuffix + url.substring(extPos);
                }
            }
        }
        final String urlPrefix = getUrlPrefix(group);
        return urlPrefix != null ? urlPrefix + url : url;
    }

    private boolean getUseMinified(ResourceGroup group) {
        return useMinified != null ? useMinified : group.getUseMinified();
    }

    private String getMinSuffix(ResourceGroup group) {
        return minSuffix != null ? minSuffix : group.getMinSuffix();
    }

    private String getUrlPrefix(ResourceGroup group) {
        return urlPrefix != null ? urlPrefix : group.getUrlPrefix();
    }

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
