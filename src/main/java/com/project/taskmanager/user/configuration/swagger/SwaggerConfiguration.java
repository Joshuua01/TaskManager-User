package com.project.taskmanager.user.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.RouterOperationCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.fn.RouterOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
class SwaggerConfiguration implements RouterOperationCustomizer, ServerBaseUrlCustomizer {

    private final String basePath;

    SwaggerConfiguration(@Value("${server.servlet.context-path}") String basePath) {
        this.basePath = basePath;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Override
    public RouterOperation customize(RouterOperation routerOperation, HandlerMethod handlerMethod) {
        routerOperation.setPath(basePath + routerOperation.getPath());
        return routerOperation;
    }

    @Override
    public String customize(String serverBaseUrl) {
        return null;
    }
}