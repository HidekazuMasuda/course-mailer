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

        if (!Validator.isMailAddress(form.getAddress())) {
            model.addAttribute("error", "error");
            return "send";
        }

        if (!Validator.isSubject(form.getSubject())) {
            model.addAttribute("error", "error");
            return "send";
        }

        if (!Validator.isBody(form.getBody())) {
            model.addAttribute("error", "error");
            return "send";
        }

        MailInfo mail = new MailInfo("gadget.mailsender@gmail.com", form.getAddress(), form.getSubject(), form.getBody());
        try {
            mailService.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "send";
    }

}
