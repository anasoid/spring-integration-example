package org.anasoid.example.spring.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication(scanBasePackages = {"org.anasoid.example.spring.integration.config.basic", "org.anasoid.example.spring.integration.config.commun"})
@EnableIntegration
public class BasicSpringIntegrationExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicSpringIntegrationExampleApplication.class, args);
    }

}
