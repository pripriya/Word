package com.geval6.word.RequestManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RequestFunctions {


    public static Object objectFromJson(String jsonString) {


        try {
            Object object= null;

            if (getJsonType(jsonString) == JSONObject.class) {
                object = new JSONObject(jsonString);
            } else {
                JSONArray object2 = new JSONArray(jsonString);
            }

            if (object.getClass() == JSONObject.class) {
                return hashMapFromJsonObject((JSONObject) object);
            } else if (object.getClass() != JSONArray.class) {
                return object;
            } else {
                return arrayListFromJsonArray((JSONArray) object);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static Class getJsonType(String jsonString) {
        if (jsonString.trim().substring(0, 1).equalsIgnoreCase("{")) {
            return JSONObject.class;
        }
        return JSONArray.class;
    }

    public static HashMap hashMapFromJsonObject(JSONObject jsonObject) {
        HashMap<String, Object> hashMap = new HashMap();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                Object object = jsonObject.get(key);
                if (object.getClass() == JSONObject.class) {
                    hashMap.put(key, hashMapFromJsonObject((JSONObject) object));
                } else if (object.getClass() == JSONArray.class) {
                    hashMap.put(key, arrayListFromJsonArray((JSONArray) object));
                } else {
                    hashMap.put(key, object);
                }
            } catch (Exception e) {
            }
        }
        return hashMap;
    }

    public static ArrayList arrayListFromJsonArray(JSONArray jsonArray) {
        ArrayList<Object> arrayList = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object object = jsonArray.get(i);
                if (object.getClass() == JSONObject.class) {
                    arrayList.add(hashMapFromJsonObject((JSONObject) object));
                } else if (object.getClass() == JSONArray.class) {
                    arrayList.add(arrayListFromJsonArray((JSONArray) object));
                } else {
                    arrayList.add(object);
                }
            } catch (Exception e) {
            }
        }
        return arrayList;
    }
}
