package jp.co.yahoo.mailsender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class ContactListController {

    @GetMapping("/contact-list")
    public String contactList(Map<String, Object> request ){

        return "contact-list";
    }


}
