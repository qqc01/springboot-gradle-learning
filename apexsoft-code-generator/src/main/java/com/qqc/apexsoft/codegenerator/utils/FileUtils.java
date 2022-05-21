package com.qqc.apexsoft.codegenerator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
    /**
     * 检查文件是否存在
     */
    public static boolean fileCheck(String url) {
        return new File(url).exists();
    }

    /**
     * 读取和clazz文件同目录下的文件
     * @param clazz
     * @param filename
     * @return
     */
    public static String readString(Class clazz, String filename) {
        String path = clazz.getResource(filename).getPath();
        StringBuilder sb = new StringBuilder();
        String context = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((context = br.readLine()) != null) {
                sb.append(context);
                sb.append("\r\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
