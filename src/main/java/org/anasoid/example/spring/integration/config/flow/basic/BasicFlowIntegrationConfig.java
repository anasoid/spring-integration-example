package org.anasoid.example.spring.integration.config.flow.basic;


import org.anasoid.example.spring.integration.splitter.CommaMessageSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;

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
 *
 */
@Configuration
public class BasicFlowIntegrationConfig {

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlow.from("sequencePollChanel")
                .split(new CommaMessageSplitter())
                .handle(System.out::println)
                .get();
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
