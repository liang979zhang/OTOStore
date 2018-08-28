package com.example.administrator.otostore.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */

public class GsonUtil {

//    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
//        Gson gson = new Gson();
//        T result = gson.fromJson(jsonData.substring(2, jsonData.length() - 2), type);
//        return result;
//    }
//
//    public static <T> List<T> parseJsonArrayWithGson(String jsonData,
//                                                     Class<T> type) {
//
//        JsonParser parser = new JsonParser();
//        JsonArray jsonArray = parser.parse(jsonData.substring(1, jsonData.length() - 1)).getAsJsonArray();
//        Gson gson = new Gson();
//        List<T> list = new ArrayList<>();
//        for (JsonElement jsonElement : jsonArray) {
//            list.add(gson.fromJson(jsonElement,type)); //cls
//        }
//        return list;
//
//    }
public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
    Gson gson = new Gson();
    T result = gson.fromJson(jsonData.substring(1, jsonData.length() - 1), type);
    return result;
}

    public static <T> List<T> parseJsonArrayWithGson(String jsonData,
                                                     Class<T> type) {

        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(jsonData).getAsJsonArray();
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            list.add(gson.fromJson(jsonElement,type)); //cls
        }
        return list;

    }
}
