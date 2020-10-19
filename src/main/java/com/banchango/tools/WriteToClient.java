package com.banchango.tools;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WriteToClient {

    public static void send(HttpServletResponse response, org.json.simple.JSONObject jsonObject, Integer statusCode) {
        try {
            response.setStatus(statusCode);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            if(jsonObject != null) {
                response.getWriter().println(jsonObject.toJSONString());
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
