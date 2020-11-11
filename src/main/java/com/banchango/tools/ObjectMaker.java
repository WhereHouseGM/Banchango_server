package com.banchango.tools;

import com.banchango.domain.users.Users;
import org.json.JSONArray;
import org.json.JSONObject;

public class ObjectMaker {

    public static JSONObject getJSONObject() {
        return new JSONObject();
    }

    public static JSONArray getJSONArray() {
        return new JSONArray();
    }

    @SuppressWarnings("unchecked")
    public static JSONObject getJSONObjectWithException(Exception exception) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", exception.getMessage());
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject getJSONObjectWithUserInfo(Users user) {
        JSONObject jsonObject = getJSONObject();
        jsonObject.put("User", user.toJSONObject());
        return jsonObject;
    }
}