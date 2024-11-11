package org.anasoid.example.spring.integration.transformer;

import org.anasoid.example.spring.integration.dto.MessagePayloadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.retry.RetryException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class TransformerMessageFromString implements GenericTransformer<Message<String>, Message<MessagePayloadDto>> {
    public final static Logger LOG = LoggerFactory.getLogger(TransformerMessageFromString.class);

    @Override
    public Message<MessagePayloadDto> transform(Message<String> source) {
        String regex = "^e(\\d+),m(\\d+),n(\\d+),o(\\d+)$";
        String line = source.getPayload();
        LOG.warn("----IN : " + source);
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(line);
        if (matcher.find()) {
            long executionId = Long.valueOf(matcher.group(1));
            Integer code = Integer.valueOf(matcher.group(2));
            int modulo = 3;
            long current = System.currentTimeMillis() % 10000;
            int deliveryAttempt = Integer.valueOf(source.getHeaders().get("deliveryAttempt").toString());
            if (current % modulo == code % modulo) {
                LOG.warn("----RETRY :(" + current + "," + code + "): " + source);
                throw new RetryException("Retry (" + current + "," + code + ")");
            }
            if (deliveryAttempt > 0) {
                LOG.info("----RETRYING Success(" + deliveryAttempt + "):(" + current + "," + code + "): " + source);
            }
            MessagePayloadDto result = new MessagePayloadDto(executionId, code);
            result.getSubMessage().put("m", Integer.valueOf(matcher.group(2)));
            result.getSubMessage().put("n", Integer.valueOf(matcher.group(3)));
            result.getSubMessage().put("o", Integer.valueOf(matcher.group(4)));
            return new GenericMessage<>(result, source.getHeaders());

        } else {
            throw new RuntimeException("Invalid Message");
        }


    }
}