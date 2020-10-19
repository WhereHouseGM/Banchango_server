package com.banchango.tools;

import com.banchango.domain.users.Users;

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

    @SuppressWarnings("unchecked")
    public static org.json.simple.JSONObject getJSONObjectWithUserInfo(Users user) {
        org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
        org.json.simple.JSONArray jsonArray = ObjectMaker.getSimpleJSONArray();
        org.json.simple.JSONObject jTemp = ObjectMaker.getSimpleJSONObject();
        jTemp.putAll(user.convertMap());
        jsonArray.add(jTemp);
        jsonObject.put("User", jsonArray);
        return jsonObject;
    }
}