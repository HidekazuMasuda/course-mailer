package jp.co.yahoo.mailsender.controller;

import jp.co.yahoo.mailsender.form.MailSendForm;
import jp.co.yahoo.mailsender.service.MailInfo;
import jp.co.yahoo.mailsender.service.MailService;
import jp.co.yahoo.mailsender.utils.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.stream.Stream;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/send")
    public String send(Model model) {
        return "send";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendEmail(@Valid @ModelAttribute MailSendForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "error");
            return "send";
        }

        String[] addresses = form.getAddress().split("\\s*;\\s*");

        boolean isValid = Stream.of(addresses)
                .anyMatch(address -> !Validator.isMailAddress(address));

        if (isValid) {
            model.addAttribute("errorMessage", "error");
            return "send";
        }

        for (String address : addresses) {
            MailInfo mail = new MailInfo("gadget.mailsender@gmail.com", address, form.getSubject(), form.getBody());
            try {
                mailService.send(mail);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "error");
                return "send";
            }
        }

        return "send";
    }

}
