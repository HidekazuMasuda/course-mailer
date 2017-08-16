package jp.co.yahoo.mailsender.controller;

import jp.co.yahoo.mailsender.form.MailSendForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MailController {

    @GetMapping("/send")
    public String send(Model model){
        return "send";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String post(@ModelAttribute MailSendForm form, Model model) {
        model.addAttribute("form", form);
        return "send";
    }

}
