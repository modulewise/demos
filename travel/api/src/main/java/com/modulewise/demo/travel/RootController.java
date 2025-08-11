package com.modulewise.demo.travel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @Value("${project.version}")
    private String projectVersion;

    @GetMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Travel API");
        apiInfo.put("version", projectVersion);
        apiInfo.put("description", "A REST API for travel bookings including flights and hotels");
        apiInfo.put("documentation", "/swagger-ui.html");
        apiInfo.put("openapi_spec", "/api-docs");
        apiInfo.put("endpoints", Map.of(
            "flights", "/flights",
            "hotels", "/hotels",
            "flight bookings", "/flights/bookings",
            "hotel bookings", "/hotels/bookings"
        ));
        return apiInfo;
    }
}
