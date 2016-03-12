package com.github.acc15.htmlres;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2016-04-02
 */
public class HtmlResMojoTest {

    @Rule
    public MojoRule rule = new MojoRule();

    @Test
    public void test() throws Exception {

        final URL resource = HtmlResMojoTest.class.getClassLoader().getResource("pom.xml");
        assert resource != null;

        final File pom = new File(resource.toURI());
        assertThat(pom).exists();

        HtmlResMojo mojo = (HtmlResMojo) rule.lookupMojo("htmlres", pom);

        mojo.execute();
    }

}