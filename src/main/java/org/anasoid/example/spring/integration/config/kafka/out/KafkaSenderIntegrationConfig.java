package org.anasoid.example.spring.integration.config.kafka.out;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;

import java.util.Map;


/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @author : anasoid
 * Date :   11/3/24
 */
@Configuration
public class KafkaSenderIntegrationConfig {

    public final static String TOPIC = "SPR_TOPIC";
    @Autowired
    KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<java.lang.Object, java.lang.Object> producerFactory() {
        Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties(null);
        producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    public IntegrationFlow myFlow(MessageHandler toKafka) {
        return IntegrationFlow.from("sequencePollChanel")
                .publishSubscribeChannel(s -> s
                        .applySequence(true)
                        .subscribe(f -> f
                                .handle(toKafka))
                        .subscribe(f -> f.
                                handle(m -> System.out.println("Send   : " + m)))
                )
                .get();
    }


    @Bean
    public MessageHandler toKafka(KafkaTemplate<String, String> kafkaTemplate) {
        KafkaProducerMessageHandler<String, String> handler =
                new KafkaProducerMessageHandler<>(kafkaTemplate);
        handler.setMessageKeyExpression(new LiteralExpression("m.key"));
        handler.setTopicExpression(new LiteralExpression(TOPIC));
        return handler;
    }


    @Bean
    public MessageChannel sequencePollChanel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "sequencePollChanel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<String> sequenceMessageSource() {
        MessageSource<String> sourceReader = new MessageSource<>() {
            int i = -1;

            @Override
            public Message<String> receive() {
                i++;
                return new GenericMessage<>("m" + i + ",n" + i + ",o" + i);
            }
        };
        return sourceReader;
    }
}
