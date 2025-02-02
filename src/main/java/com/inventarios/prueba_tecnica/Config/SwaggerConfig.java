package com.inventarios.prueba_tecnica.Config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {

        @Bean
        public GroupedOpenApi publicApi() {
                return GroupedOpenApi.builder()
                                .group("public")
                                .pathsToMatch("/**")
                                .addOperationCustomizer((operation, handlerMethod) -> operation
                                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")))
                                .build();
        }

        @Bean
        public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
                return new io.swagger.v3.oas.models.OpenAPI()
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .name("Authorization")
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .in(SecurityScheme.In.HEADER)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}
