package com.parfait.study.simplewebsocket.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("sockjs")
@Component
public class ChatHandler extends TextWebSocketHandler {

    private final Map<String, ChatRoom> chatRoomMap;
    @Getter
    private final Collection<ChatRoom> chatRooms;
    private final ObjectMapper objectMapper;
    private final Map<MessageType, ChatSender> typeSenderMap;

    @Autowired
    public ChatHandler(ObjectMapper objectMapper, List<ChatSender> senders) {
        this.objectMapper = objectMapper;
        chatRoomMap = Collections.unmodifiableMap(
                Stream.of(ChatRoom.create("1번방"), ChatRoom.create("2번방"), ChatRoom.create("3번방"))
                      .collect(Collectors.toMap(ChatRoom::getId, Function.identity())));

        chatRooms = Collections.unmodifiableCollection(chatRoomMap.values());

        typeSenderMap = Collections.unmodifiableMap(senders.stream().collect(Collectors.toMap(ChatSender::supportType, Function.identity())));
    }

    public ChatRoom getChatRoom(String id) {
        return chatRoomMap.get(id);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        chatMessage.setWriter(session.getId());
        typeSenderMap.get(chatMessage.getType()).send(chatMessage, getChatRoom(chatMessage.getChatRoomId()), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatRooms.parallelStream().forEach(chatRoom -> chatRoom.remove(session));
    }
}
