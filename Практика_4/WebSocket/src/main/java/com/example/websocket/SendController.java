package com.example.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class SendController {


    @MessageMapping("/webs")
    @SendTo("/topic/webs")
    public Greeting greeting(Message message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Server: "+HtmlUtils.htmlEscape(message.getName()));
    }

}
