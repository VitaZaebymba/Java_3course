package com.example.demo.controller;

import com.example.demo.entiy.Message;
import com.example.demo.entiy.User;
import com.example.demo.repos.MessageRepo;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageRepo messageRepo;
    @GetMapping("/")
    public String getChat(@AuthenticationPrincipal User user, Model model){
        User userBD = userRepo.findById(user.getId()).orElse(new User());
        List<Message> messageBDList=messageRepo.findAll();
        model.addAttribute("messages",messageBDList);
        model.addAttribute("username",userBD.getUsername());
        return "chat";
    }
    @MessageMapping("/webs")
    @SendTo("/topic/webs")
    public Message greeting(Message message) throws Exception {
        message.setTime(new SimpleDateFormat("yyyy.MM.dd:HH.mm.ss").format(Calendar.getInstance().getTime()));
        messageRepo.save(message);
        Thread.sleep(1000);
        return message;
    }
}
