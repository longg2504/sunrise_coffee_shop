package com.cg.controller;

import com.cg.domain.dto.socket.Notification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSocketController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/notification")
    public Notification sendMessage(@Payload Notification notification) {
        System.out.println("SOCKET send data from client: " + notification);
//        chatMessage.setContent(chatMessage.getContent() + " love");
        return notification;
    }

    @RequestMapping("/testsocket")
    public String testSocket(){
        return "socket";
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/publicChatRoom")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }

}