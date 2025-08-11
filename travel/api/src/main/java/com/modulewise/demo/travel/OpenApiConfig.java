package com.modulewise.demo.travel;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class OpenApiConfig {

    @Value("${project.version}")
    private String projectVersion;

    @Bean
    public OpenAPI travelApiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("Travel API")
                .description("A REST API for travel bookings including flights and hotels")
                .version(projectVersion));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("travel-api")
            .pathsToMatch("/flights/**", "/hotels/**")
            .build();
    }
}
