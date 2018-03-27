package com.parfait.study.simplewebsocket.chatroom;

import org.springframework.web.socket.WebSocketSession;

public interface ChatSender {

    MessageType supportType();

    void send(ChatMessage chatMessage, ChatRoom chatRoom, WebSocketSession session);
}
