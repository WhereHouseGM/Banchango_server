package com.banchango.tools;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.JSONObject;

public class WriteToClient {

    public static void send(HttpServletResponse response, JSONObject jsonObject, Integer statusCode) {
        try {
            response.setStatus(statusCode);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(jsonObject.toString());
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
