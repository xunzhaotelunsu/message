package com.baosight.message.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

public class JSONUtil {

    public static boolean isJSON(String str){
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            new JsonParser().parse(str);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static String prettyJson(String str){
        if(isJSON(str)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Object origin = gson.fromJson(str, Object.class);
            return gson.toJson(origin);
        }else{
            return str;
        }
    }
}
