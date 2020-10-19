package com.banchango.tools;

public class ObjectMaker {

    public static org.json.simple.JSONObject getSimpleJSONObject() {
        return new org.json.simple.JSONObject();
    }

    public static org.json.simple.JSONArray getSimpleJSONArray() {
        return new org.json.simple.JSONArray();
    }

    @SuppressWarnings("unchecked")
    public static org.json.simple.JSONObject getJSONObjectWithException(Exception exception) {
        org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
        jsonObject.put("message", exception.getMessage());
        return jsonObject;
    }
}