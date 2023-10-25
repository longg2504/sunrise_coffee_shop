package com.cg.controller;

import com.cg.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {



    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/notification")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        System.out.println("RECEIVED: " + chatMessage);
        chatMessage.setContent(chatMessage.getContent() + " love lyly");
        return chatMessage;
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/publicChatRoom")
//    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }

}