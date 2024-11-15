package org.anasoid.example.spring.integration.splitter;

import org.anasoid.example.spring.integration.dto.MessagePayloadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
public class BasicMessageSplitter extends AbstractMessageSplitter {
    public final static Logger LOG = LoggerFactory.getLogger(BasicMessageSplitter.class);

    @Override
    protected Iterator<Message<MessagePayloadDto>> splitMessage(Message<?> message) {
        LOG.info("SPLIT : " + message);
        return new MessageIterator((Message<MessagePayloadDto>) message);
    }

    class MessageIterator implements Iterator<Message<MessagePayloadDto>> {

        final Message<MessagePayloadDto> originMessage;

        final Iterator<Map.Entry<String, Integer>> internalIterator;

        public MessageIterator(Message<MessagePayloadDto> originMessagePayloadDto) {
            this.originMessage = originMessagePayloadDto;
            internalIterator = originMessagePayloadDto.getPayload().getSubMessage().entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        @Override
        public Message<MessagePayloadDto> next() {
            Map.Entry<String, Integer> entry = internalIterator.next();
            MessagePayloadDto payloadDto = new MessagePayloadDto(originMessage.getPayload().getExecutionId(), originMessage.getPayload().getCode());
            payloadDto.getSubMessage().put(entry.getKey(), entry.getValue());
            Map<String,Object> messageHeaders = copyMessageHeaders(originMessage.getHeaders(), null);

            messageHeaders.put("SPLITTER", true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new GenericMessage<>(payloadDto, messageHeaders);
        }

        /**
         * Copy constructor which allows for ignoring certain entries.
         * Used for serialization without non-serializable entries.
         *
         * @param original     the MessageHeaders to copy
         * @param keysToIgnore the keys of the entries to ignore
         */
        private Map<String,Object> copyMessageHeaders(MessageHeaders original, Set<String> keysToIgnore) {
            Map<String,Object> headers = new HashMap<>();
            original.forEach((key, value) -> {
                if ((keysToIgnore == null || !keysToIgnore.contains(key)) && (value instanceof Serializable)) {
                    headers.put(key, value);
                }
            });
            return headers;
        }

    }
}
