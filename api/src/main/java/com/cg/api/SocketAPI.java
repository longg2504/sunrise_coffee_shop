package com.cg.api;

import com.cg.domain.dto.socket.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/socket")
public class SocketAPI {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @GetMapping
    public ResponseEntity<?> sendMessage(){


        Notification chatMessage = new Notification();
        chatMessage.setType(Notification.NotificationType.KITCHEN);
        chatMessage.setSender("GỬI THƯ CAI CHI DO");

        messagingTemplate.convertAndSend("/topic/publicChatRoom", chatMessage);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
