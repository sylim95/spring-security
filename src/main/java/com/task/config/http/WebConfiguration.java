package com.task.config.http;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT"; // JWT Authorize 추가
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWt")
        );

        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Spring security Project")
                        .description("Spring security API Specification")
                        .version("v.1.0.0"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
