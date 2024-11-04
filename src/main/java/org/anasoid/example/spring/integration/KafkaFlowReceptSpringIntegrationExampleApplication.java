package org.anasoid.example.spring.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication(scanBasePackages = {"org.anasoid.example.spring.integration.config.kafka.out"})
@EnableIntegration
public class KafkaFlowReceptSpringIntegrationExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaFlowReceptSpringIntegrationExampleApplication.class, args);
    }

}
