package org.anasoid.example.spring.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication(scanBasePackages = {"org.anasoid.example.spring.integration.config.kafka"})
@EnableIntegration
public class KafkaFlowSpringIntegrationExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaFlowSpringIntegrationExampleApplication.class, args);
    }

}
