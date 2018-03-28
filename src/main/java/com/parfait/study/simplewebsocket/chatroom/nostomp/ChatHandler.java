package com.parfait.study.simplewebsocket.chatroom.nostomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parfait.study.simplewebsocket.chatroom.model.ChatMessage;
import com.parfait.study.simplewebsocket.chatroom.nostomp.service.MessageTypeChatMessageHandlerRouter;
import com.parfait.study.simplewebsocket.chatroom.repository.ChatRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Profile("!stomp")
@Component
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MessageTypeChatMessageHandlerRouter chatMessageHandlerRouter;
    private final ChatRoomRepository repository;

    @Autowired
    public ChatHandler(ObjectMapper objectMapper, MessageTypeChatMessageHandlerRouter chatMessageHandlerRouter, ChatRoomRepository chatRoomRepository) {
        this.objectMapper = objectMapper;
        this.chatMessageHandlerRouter = chatMessageHandlerRouter;
        this.repository = chatRoomRepository;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        log.info("payload : {}", payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        chatMessage.setWriter(session.getId());
        chatMessageHandlerRouter.get(chatMessage.getType()).send(chatMessage, repository.getChatRoom(chatMessage.getChatRoomId()), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        repository.remove(session);
    }
}
