package com.parfait.study.simplewebsocket.chatroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile("sockjs")
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatHandler handler;

    @Autowired
    public ChatRoomController(ChatHandler handler) {
        this.handler = handler;
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", handler.getChatRooms());
        return "/chat1/rooms";
    }

    @GetMapping("/rooms/{id}")
    public String room(@PathVariable String id, Model model) {
        ChatRoom room = handler.getChatRoom(id);
        model.addAttribute("room", room);

        return "/chat1/room-detail";
    }
}
