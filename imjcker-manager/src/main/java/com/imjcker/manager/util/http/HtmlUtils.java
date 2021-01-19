package com.imjcker.manager.util.http;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by <a href='https://github.com/imjcker'>Alan Turing</a>
 * on 2018-02-25 at 3:48 AM
 *
 * @version 1.0
 */
public class HtmlUtils {
    /**
     * 提取html中的文字
     *
     * @param html
     * @return
     */
    public static String htmlToText(String html) {
        if (StringUtils.isNotEmpty(html)) {
            return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        return "";
    }

}
