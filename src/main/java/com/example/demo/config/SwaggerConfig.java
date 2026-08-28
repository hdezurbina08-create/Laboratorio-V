package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Laboratorio Spring Boot")
                        .version("1.0")
                        .description("Documentación interactiva con Swagger UI para los recursos de Productos, Estudiantes, Libros y Tareas."));
    }
}
