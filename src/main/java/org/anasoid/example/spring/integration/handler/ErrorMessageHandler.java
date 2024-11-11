package org.anasoid.example.spring.integration.handler;

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
 * Date :   11/6/24
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.ErrorMessage;

public class ErrorMessageHandler implements MessageHandler {

    public final static Logger LOG = LoggerFactory.getLogger(ErrorMessageHandler.class);

    @Override
    public void handleMessage(Message<?> inMessage) throws MessagingException {
        Message<?> message = getOriginalMessage(inMessage);

        Acknowledgment acknowledgment = (Acknowledgment) message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT);
        if (acknowledgment != null) {
            LOG.error("!!!! WARN : " + message);
            //        acknowledgment.nack(Duration.ofSeconds(6));
        }
        LOG.error("!!!! ERROR : " + message);
    }

    Message<?> getOriginalMessage(Message<?> message) {
        if (message instanceof ErrorMessage) {
            Message<?> originMessage = ((ErrorMessage) message).getOriginalMessage();
            if (originMessage == null) {
                return message;
            } else {
                return getOriginalMessage(originMessage);
            }

        } else
            return message;
    }

}
