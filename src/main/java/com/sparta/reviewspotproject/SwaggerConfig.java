package com.sparta.reviewspotproject;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("ReviewSpot API Test") // API의 제목
                .description("뉴스피드 프로젝트 - ReviewSpot API") // API에 대한 설명
                .version("0.0.1"); // API의 버전
    }
}