package com.github.acc15.htmlres;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private List<HtmlResource> jsResources;

    @Parameter
    private List<HtmlResource> cssResources;

    private final Map<String, HtmlResourceGroup> groupMap = new HashMap<>();
    private final Set<HtmlResourceGroup> flatGroups = new HashSet<>();

    public void execute() throws MojoExecutionException {
        if (groups != null) {
            groups.stream().filter(group -> group.getId() != null).forEach(group -> {
                groupMap.put(group.getId(), group);
            });
            groups.forEach(this::flattenGroup);
        }
        processGroup(this);
        groups.forEach(this::processGroup);
    }

    private void processGroup(ResourceGroup group) {
        if (group.getTargetFile() == null) {
            // skipping groups without target
            return;
        }
        if (group.getTemplate() == null) {
            throw new IllegalArgumentException("missing template file");
        }

        final LinkedHashSet<String> jsUrls = extractUrls(group.getJsResources(), group);
        final LinkedHashSet<String> cssUrls = extractUrls(group.getCssResources(), group);

        try {

            final Document document = Jsoup.parse(group.getTemplate(), "utf-8");
            final Elements elements = document.getElementsByAttribute("data-maven-htmlres");
            for (Element element : elements) {
                final String nodeName = element.nodeName();

                Element e = element;
                switch (nodeName) {
                    case "link":
                        for (String cssUrl : cssUrls) {
                            final Element link = document.createElement("link").attr("href", cssUrl).attr("rel", "stylesheet");
                            e.after(link);
                            e = link;
                        }
                        break;

                    case "script":
                        for (String jsUrl : jsUrls) {
                            final Element script = document.createElement("script").attr("src", jsUrl);
                            e.after(script);
                            e = script;
                        }
                        break;

                    default:
                        getLog().warn("Unsupported html tag: " + nodeName);
                        break;

                }
                element.remove();
            }

            FileUtils.fileWrite(group.getTargetFile(), document.outputSettings(new Document.OutputSettings().
                    indentAmount(4).
                    prettyPrint(true).
                    syntax(Document.OutputSettings.Syntax.html).
                    charset("utf-8").
                    outline(true)).outerHtml());

        } catch (IOException e) {
            getLog().error(e);
        }
    }

    private LinkedHashSet<String> extractUrls(List<HtmlResource> resources, ResourceGroup group) {
        final LinkedHashSet<String> urls = new LinkedHashSet<>();
        if (resources == null) {
            return urls;
        }
        for (HtmlResource resource : resources) {
            if (resource.getUrl() != null) {
                urls.add(getTargetUrl(resource.getUrl(), resource, group));
            } else if (resource.getDir() != null) {
                final DirectoryScanner scanner = new DirectoryScanner();
                scanner.setBasedir(resource.getDir());
                if (resource.getIncludes() != null) {
                    scanner.setIncludes(resource.getIncludes());
                }
                if (resource.getExcludes() != null) {
                    scanner.setExcludes(resource.getExcludes());
                }
                scanner.scan();

                urls.addAll(Arrays.asList(scanner.getIncludedFiles()).stream().
                        map(s -> getTargetUrl(s.replace('\\', '/'), resource, group)).
                        collect(Collectors.toList()));
            }
        }
        return urls;
    }

    private String getTargetUrl(String url, HtmlResource resource, ResourceGroup group) {
        final boolean useMinified = resource.getUseMinified() != null ? resource.getUseMinified() : group.getUseMinified();
        if (useMinified) {
            final String minSuffix = resource.getMinSuffix() != null ? resource.getMinSuffix() : group.getMinSuffix();
            if (minSuffix != null) {
                final int extPos = url.lastIndexOf('.');
                if (extPos < 0) {
                    url = url + minSuffix;
                } else {
                    url = url.substring(0, extPos) + minSuffix + url.substring(extPos);
                }
            }
        }
        final String urlPrefix = resource.getUrlPrefix() != null ? resource.getUrlPrefix() : group.getUrlPrefix();
        return urlPrefix != null ? urlPrefix + url : url;
    }

    private void flattenGroup(HtmlResourceGroup group) {
        if (flatGroups.contains(group)) {
            // group already flat
            return;
        }

        ResourceGroup parent = this;
        if (group.getParent() != null) {
            final HtmlResourceGroup parentGroup = groupMap.get(group.getParent());
            if (parentGroup == null) {
                throw new IllegalArgumentException("illegal parent group id: " + group.getParent());
            }
            flattenGroup(parentGroup);
            parent = parentGroup;
        }
        if (group.getUseMinified() == null) {
            group.setUseMinified(parent.getUseMinified());
        }
        if (group.getUrlPrefix() == null) {
            group.setUrlPrefix(parent.getUrlPrefix());
        }
        if (group.getMinSuffix() == null) {
            group.setMinSuffix(parent.getMinSuffix());
        }
        if (group.getTargetFile() == null) {
            group.setTargetFile(parent.getTargetFile());
        }
        if (group.getTemplate() == null) {
            group.setTemplate(parent.getTemplate());
        }
        if (group.getJsResources() == null) {
            group.setJsResources(parent.getJsResources());
        } else if (parent.getJsResources() != null) {
            group.getJsResources().addAll(0, parent.getJsResources());
        }
        if (group.getCssResources() == null) {
            group.setCssResources(parent.getCssResources());
        } else if (parent.getCssResources() != null) {
            group.getCssResources().addAll(0, parent.getCssResources());
        }
        flatGroups.add(group);
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
