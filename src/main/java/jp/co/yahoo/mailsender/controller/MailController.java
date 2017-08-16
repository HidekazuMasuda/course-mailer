package jp.co.yahoo.mailsender.controller;

import jp.co.yahoo.mailsender.form.MailSendForm;
import jp.co.yahoo.mailsender.service.MailInfo;
import jp.co.yahoo.mailsender.service.MailService;
import jp.co.yahoo.mailsender.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MailController {

    @Autowired
    public MailService mailService;

    @GetMapping("/send")
    public String send(Model model){
        return "send";
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public String sendEmail(@ModelAttribute MailSendForm form, Model model) {
        MailInfo mail = new MailInfo("", "", form.getSubject(), "");
        try {
            mailService.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "send";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String post(@ModelAttribute MailSendForm form, Model model) {
        // TODO Validate Request
        String address = form.getAddress();
        boolean isMailAddress = Validator.isMailAddress(address);
        if (!isMailAddress) {
            model.addAttribute("form", form);
            model.addAttribute("error", "error");
            return "send";
        }

        String subject = form.getSubject();
        boolean isSubject = Validator.isSubject(subject);

        String body = form.getBody();
        boolean isBody = Validator.isBody(body);

        // TODO Mail Send

        // TODO Show the page
        model.addAttribute("form", form);
        return "send";
    }

}
