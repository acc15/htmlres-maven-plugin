package com.github.acc15.htmlres;

import java.io.File;
import java.util.List;

/**
 * Created by acc15 on 12.03.16.
 */
public class HtmlResourceGroup {

    String id;
    String parent;
    boolean useMinified;
    String minSuffix;
    File targetFile;
    List<HtmlResource> jsResources;
    List<HtmlResource> cssResources;

}
