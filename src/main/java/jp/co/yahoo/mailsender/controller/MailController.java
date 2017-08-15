package jp.co.yahoo.mailsender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class MailController {

    @GetMapping("/send")
    public String send(Map<String, Object> request ){

        return "send";
    }


}
