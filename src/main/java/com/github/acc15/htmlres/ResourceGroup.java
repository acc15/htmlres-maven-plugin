package com.github.acc15.htmlres;

import java.io.File;
import java.util.List;

/**
 * Created by acc15 on 12.03.16.
 */
public interface ResourceGroup {

    String getUrlPrefix();
    Boolean getUseMinified();
    String getMinSuffix();
    File getTemplate();
    File getTargetFile();
    List<HtmlResource> getJsResources();
    List<HtmlResource> getCssResources();

}
