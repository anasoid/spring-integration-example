package org.anasoid.example.spring.integration.dto;

import java.util.HashMap;
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
 * Date :   11/5/24
 */
public class MessagePayloadDto {
    private long executionId;
    private int code;

    public MessagePayloadDto(long executionId, int code) {
        this.executionId = executionId;
        this.code = code;
    }

    private Map<String, Integer> subMessage = new HashMap<>();

    public long getExecutionId() {
        return executionId;
    }

    public int getCode() {
        return code;
    }

    public Map<String, Integer> getSubMessage() {
        return subMessage;
    }

    @Override
    public String toString() {
        return "{" +
                "executionId=" + executionId +
                ", code=" + code +
                ", subMessage=" + subMessage +
                '}';
    }
}
