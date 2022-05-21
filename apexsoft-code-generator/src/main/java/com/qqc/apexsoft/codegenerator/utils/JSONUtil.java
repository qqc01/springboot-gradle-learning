package com.qqc.apexsoft.codegenerator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtil {
    public static String getJSONStandardString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat);
    }
}
