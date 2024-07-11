package fr.diginamic.springdemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {

    /**
     * Bean to create a RestTemplate
     * @return the RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Bean to create a custom OpenAPI object
     * @return the OpenAPI object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Recensement")
                        .version("1.0")
                        .description("API pour le recensement des villes françaises")
                        .termsOfService("OPEN DATA")
                        .contact(new Contact()
                                .name("Ayoub Benziza")
                                .email("test@email.com")
                                .url("https://www.diginamic.fr")));
    }
}
