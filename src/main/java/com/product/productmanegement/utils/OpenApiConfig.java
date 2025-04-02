package com.product.productmanegement.utils;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI categoryManagementOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Category Management API")
                .description("API for multi-level category management system")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Rishabhmishra8118")
                    .email("admin@example.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
            .externalDocs(new ExternalDocumentation()
                .description("Category Management Documentation")
                .url("https://github.com/yourusername/category-management"))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Development Server"),
                new Server().url("https://api.example.com").description("Production Server")
            ));
    }
}