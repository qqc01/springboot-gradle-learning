package com.qqc.apexsoft.codegenerator.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
    public static String upper(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return String.valueOf(str.charAt(0)).toUpperCase().concat(str.substring(1));
    }
}
