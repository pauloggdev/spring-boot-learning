package api.algorithm_engine.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import api.algorithm_engine.model.ChatMessage;

@Controller
public class ChatController {

    @MessageMapping("/chat") // /app/chat
    @SendTo("/topic/messages") // broadcast
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}