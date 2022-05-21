package com.qqc.apexsoft.codegenerator.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class StringHelper {
    private String path = "";
    public static final StringHelper INSTANCE = new StringHelper();

    public StringHelper concatPath(String str) {
        if (!StringUtils.isBlank(str)) {
            append(File.separator.concat(str));
        }
        return this;
    }

    public StringHelper concat(String str) {
        if (!StringUtils.isBlank(str)) {
            append(str);
        }
        return this;
    }

    public StringHelper replace(String str) {
        this.path = str.replace(".", "\\");
        return this;
    }

    public String getPath() {
        String path = this.path;
        clear();
        return path;
    }

    public void append(String var) {
        this.path = this.path.concat(var);
    }

    private void clear() {
        this.path = "";
    }

    public static StringHelper getInstance() {
        INSTANCE.clear();
        return INSTANCE;
    }
}
