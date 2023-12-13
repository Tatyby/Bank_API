package com.sberStart.bank_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
        info = @Info(
                title = "Bank_API",
                description = "bank", version = "1.0.0",
                contact = @Contact(
                        name = "Buanova Tatyana",
                        email = "TSergBuyanova@sberbank.ru"
                )
        )
)
public class OpenApiConfig {
}

