package com.ptip.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("P-Tip API Documentation")
                        .description("P-Tip 프로젝트의 API 문서")
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("accessToken")) // ✅ 자동 헤더 입력 창 추가
                .components(
                        new Components()
                                // accessToken이라는 스키마 만들어주기
                                .addSecuritySchemes("accessToken", new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .bearerFormat("JWT")
                                )
                                // refreshToken이라는 스키마 만들어주기
                                .addSecuritySchemes("refreshToken", new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .bearerFormat("JWT")
                                )
                );
    }
}
