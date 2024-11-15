package org.anasoid.example.spring.integration.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class LogChannelInterceptor implements ChannelInterceptor {

    public final static Logger LOG = LoggerFactory.getLogger(LogChannelInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // LOG.info(">>>>preSend({}, {})", message, channel);
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // LOG.info(">>>>postSend({}, {},{})", message, channel, sent);

    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        if (ex == null) {
            // LOG.info(">>>>afterSendCompletion({}, {},{})", message, channel, sent);
        } else {
            // LOG.error(">>>>afterSendCompletion({}, {},{},{},{})", message, channel, sent, ex.getMessage(), ex.getCause() == null ? "null" : ex.getCause().getMessage());
        }

    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        // LOG.info(">>>>preReceive({})", channel);
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        // LOG.info(">>>>postReceive({}, {})", message, channel);
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        if (ex == null) {
            // LOG.info(">>>>afterReceiveCompletion({}, {})", message, channel);
        } else {
            // LOG.error(">>>>afterReceiveCompletion({}, {},{},{})", message, channel, ex.getMessage(), ex.getCause() == null ? "null" : ex.getCause().getMessage());
        }

    }
}
