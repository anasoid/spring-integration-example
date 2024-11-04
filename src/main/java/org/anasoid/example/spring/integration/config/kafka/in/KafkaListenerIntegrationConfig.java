package org.anasoid.example.spring.integration.config.kafka.in;

import org.anasoid.example.spring.integration.config.kafka.out.KafkaSenderIntegrationConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.support.RawRecordHeaderErrorMessageStrategy;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.support.RetryTemplate;

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
public class KafkaListenerIntegrationConfig {

    public final static String TOPIC = KafkaSenderIntegrationConfig.TOPIC;

    @Autowired
    KafkaProperties kafkaProperties;

    @Bean
    public IntegrationFlow topic1ListenerFromKafkaFlow() {
        return IntegrationFlow
                .from(Kafka.messageDrivenChannelAdapter(consumerFactory(),
                                KafkaMessageDrivenChannelAdapter.ListenerMode.record, TOPIC)
                        .configureListenerContainer(c ->
                                c.ackMode(ContainerProperties.AckMode.MANUAL)
                                        .id("topic1ListenerContainer"))
                        .recoveryCallback(new ErrorMessageSendingRecoverer(errorChannel(),
                                new RawRecordHeaderErrorMessageStrategy()))
                        .retryTemplate(new RetryTemplate())
                        .filterInRetry(true))
                .channel(c -> c.queue("listeningFromKafkaResults1"))
                .get();
    }

    @Bean
    public IntegrationFlow myFlowResult() {
        return IntegrationFlow.from("listeningFromKafkaResults1")
                .handle(m -> System.out.println("Reception : " + m))
                .get();
    }

    @Bean
    public MessageChannel errorChannel() {
        return new QueueChannel();
    }

    @Bean
    public ConsumerFactory<?, ?> consumerFactory() {
        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties(null);
        consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

}
