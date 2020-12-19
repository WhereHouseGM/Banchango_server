package com.banchango.healthcheck;

import com.banchango.tools.WriteToClient;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public void responseToHealthCheck(HttpServletResponse response) {
        WriteToClient.send(response, new JSONObject(), HttpServletResponse.SC_OK);
    }
}
