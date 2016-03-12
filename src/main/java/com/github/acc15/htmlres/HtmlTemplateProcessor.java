package com.github.acc15.htmlres;

import org.codehaus.plexus.util.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by acc15 on 12.03.16.
 */
public class HtmlTemplateProcessor {

    public void substituteUrls(File templateFile, File targetFile, Iterable<String> jsUrls, Iterable<String> cssUrls) throws IOException {
        final Document document = Jsoup.parse(templateFile, "utf-8");
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
                    break;

            }
            element.remove();
        }

        FileUtils.fileWrite(targetFile, document.outputSettings(new Document.OutputSettings().
                indentAmount(4).
                prettyPrint(true).
                syntax(Document.OutputSettings.Syntax.html).
                charset("utf-8").
                outline(true)).outerHtml());

    }

}
