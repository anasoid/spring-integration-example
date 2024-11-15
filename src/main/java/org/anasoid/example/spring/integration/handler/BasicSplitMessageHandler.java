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

import org.anasoid.example.spring.integration.dto.MessagePayloadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class BasicSplitMessageHandler implements MessageHandler {

    public final static Logger LOG = LoggerFactory.getLogger(BasicSplitMessageHandler.class);

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Message<MessagePayloadDto> messageDto = (Message<MessagePayloadDto>) message;

        //LOG.info("----Reception : START : " + message);
        if (messageDto.getPayload().getSubMessage().get("m") != null) {
          //  throw new RuntimeException("unsupported message m");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOG.info("#####Reception SPLIT: " + message);
    }
}