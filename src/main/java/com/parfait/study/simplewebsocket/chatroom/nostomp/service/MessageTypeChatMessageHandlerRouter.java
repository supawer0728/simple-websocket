package com.parfait.study.simplewebsocket.chatroom.nostomp.service;

import com.parfait.study.simplewebsocket.chatroom.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Profile("!stomp")
@Service
public class MessageTypeChatMessageHandlerRouter {

    private final Map<MessageType, ChatMessageHandler> valueMap;

    @Autowired
    public MessageTypeChatMessageHandlerRouter(List<ChatMessageHandler> handlers) {
        this.valueMap = handlers.stream().collect(Collectors.toMap(ChatMessageHandler::supportType, Function.identity()));
    }

    public ChatMessageHandler get(MessageType messageType) {
        return valueMap.get(messageType);
    }
}
