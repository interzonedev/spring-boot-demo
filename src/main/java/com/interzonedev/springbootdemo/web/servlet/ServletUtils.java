package com.interzonedev.springbootdemo.web.servlet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServletUtils {

    private static final Pattern assetsPattern = Pattern.compile("^(/css/|/img/|/js/)");

    public static boolean isAssetRequest(String requestUri) {
        Matcher assetsMatcher = assetsPattern.matcher(requestUri);
        return assetsMatcher.find();
    }

}
