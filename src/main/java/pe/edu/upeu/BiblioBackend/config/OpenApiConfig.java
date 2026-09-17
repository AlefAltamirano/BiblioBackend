package pe.edu.upeu.BiblioBackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BiblioBackend API - Sistema de Gestión de Biblioteca")
                        .version("1.0.0")
                        .description("API RESTful para el control y administración de libros, socios y préstamos.")
                        .contact(new Contact()
                                .name("UPeU - Escuela de Ingeniería de Sistemas")
                                .email("soporte@upeu.edu.pe"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}