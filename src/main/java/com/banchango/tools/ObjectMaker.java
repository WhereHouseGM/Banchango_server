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

    public static JSONObject getJSONObjectWithException(Exception exception) {
        JSONObject jsonObject = getJSONObject();
        jsonObject.put("message", exception.getMessage());
        return jsonObject;
    }

    public static JSONObject getJSONObjectWithUserInfo(Users user) {
        JSONObject jsonObject = getJSONObject();
        jsonObject.put("User", user.toJSONObject());
        return jsonObject;
    }

    public static JSONObject getJSONObjectOfBadRequest() {
        JSONObject jsonObject = getJSONObject();
        jsonObject.put("message", "요청 형식이 잘못되었습니다.");
        return jsonObject;
    }
}