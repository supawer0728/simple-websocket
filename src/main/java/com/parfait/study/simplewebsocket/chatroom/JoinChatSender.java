package com.parfait.study.simplewebsocket.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class JoinChatSender implements ChatSender {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public MessageType supportType() {
        return MessageType.JOIN;
    }

    @Override
    public void send(ChatMessage chatMessage, ChatRoom chatRoom, WebSocketSession session) {
        String writerId = chatMessage.getWriter();
        chatMessage.setMessage(writerId + "님이 입장하셨습니다.");
        chatRoom.add(session);
        Set<WebSocketSession> sessions = chatRoom.getSessions();

        try {
            TextMessage sendMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage));
            sessions.parallelStream()
                    .filter(target -> !target.equals(session))
                    .forEach(target -> MessageSendUtils.sendMessage(target, sendMessage));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
