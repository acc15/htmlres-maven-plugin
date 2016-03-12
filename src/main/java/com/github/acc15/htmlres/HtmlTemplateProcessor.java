package com.github.acc15.htmlres;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by acc15 on 12.03.16.
 */
public class HtmlTemplateProcessor {

    private static final String[] TOKENS = { "<!-- ${maven.htmlres.js} -->", "<!-- ${maven.htmlres.css} -->" };

    private static class TokenMatcher {

        private int matchPos = 0;
        private int matchSet = 0;

        private final int all;

        private final Writer writer;
        private final String[] tokens;

        public TokenMatcher(Writer writer, String[] tokens) {
            this.writer = writer;
            this.tokens = tokens;
            this.all = (1 << tokens.length) - 1;
            this.matchSet = all;
        }

        public int match(char ch) throws IOException {
            for (int i=0; i<tokens.length; i++) {
                final int tokenMask = (1 << i);
                if ((matchSet & tokenMask) == 0) {
                    continue;
                }
                final String token = tokens[i];
                if (token.charAt(matchPos) != ch) {
                    matchSet &= ~tokenMask;
                    if (matchSet == 0) {
                        writer.append(token, 0, matchPos).append(ch);
                        break;
                    }
                    continue;
                }
                if (matchPos + 1 >= token.length()) {
                    matchPos = 0;
                    matchSet = all;
                    return i;
                }
            }
            if (matchSet == 0) {
                matchPos = 0;
                matchSet = all;
            } else {
                ++matchPos;
            }
            return -1;
        }
    }

    public void substituteUrls(ResourceGroup group) throws IOException {

        final LinkedHashSet<String> jsUrls = extractUrls(group.getJsResources(), group);
        final LinkedHashSet<String> cssUrls = extractUrls(group.getCssResources(), group);

        final char[] readBuffer = new char[4096];
        try (final Reader reader = new InputStreamReader(
                new FileInputStream(group.getTemplate()), group.getTemplateEncoding())) {

            try (final Writer writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(group.getTargetFile()), group.getTargetEncoding()))) {
                final TokenMatcher tokenMatcher = new TokenMatcher(writer, TOKENS);
                int readed;
                do {
                    readed = reader.read(readBuffer);
                    for (int i=0; i<readed; i++) {
                        final char ch = readBuffer[i];
                        switch (tokenMatcher.match(ch)) {
                            case 0:
                                writeJsUrls(writer, jsUrls);
                                break;

                            case 1:
                                writeCssUrls(writer, cssUrls);
                                break;
                        }

                    }
                } while (readed != -1);
            }
        }
    }

    private static void writeJsUrls(Writer writer, Iterable<String> urls) throws IOException {
        for (String url: urls) {
            writer.append("<script src=\"").append(url).append("\"></script>");
        }
    }

    private static void writeCssUrls(Writer writer, Iterable<String> urls) throws IOException {
        for (String url: urls) {
            writer.append("<link href=\"").append(url).append("\" rel=\"stylesheet\">");
        }
    }

    private static LinkedHashSet<String> extractUrls(List<HtmlResource> resources, ResourceGroup group) {
        final LinkedHashSet<String> urls = new LinkedHashSet<>();
        if (resources != null) {
            resources.forEach(r -> r.extractUrls(urls, group));
        }
        return urls;
    }

}
