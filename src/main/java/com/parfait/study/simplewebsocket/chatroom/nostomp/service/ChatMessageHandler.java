package com.parfait.study.simplewebsocket.chatroom.nostomp.service;

import com.parfait.study.simplewebsocket.chatroom.model.ChatMessage;
import com.parfait.study.simplewebsocket.chatroom.model.ChatRoom;
import com.parfait.study.simplewebsocket.chatroom.model.MessageType;
import org.springframework.web.socket.WebSocketSession;

public interface ChatMessageHandler {

    MessageType supportType();

    void send(ChatMessage chatMessage, ChatRoom chatRoom, WebSocketSession session);
}
