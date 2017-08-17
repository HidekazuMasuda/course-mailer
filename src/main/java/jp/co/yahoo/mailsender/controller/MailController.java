package jp.co.yahoo.mailsender.controller;

import jp.co.yahoo.mailsender.form.MailSendForm;
import jp.co.yahoo.mailsender.service.MailInfo;
import jp.co.yahoo.mailsender.service.MailService;
import jp.co.yahoo.mailsender.utils.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/send")
    public String send(Model model){
        return "send";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendEmail(@ModelAttribute MailSendForm form, Model model) {

        if (!Validator.isSubject(form.getSubject())) {
            model.addAttribute("errorMessage", "error");
            return "send";
        }

        if (!Validator.isBody(form.getBody())) {
            model.addAttribute("errorMessage", "error");
            return "send";
        }

        String addressFromForm = form.getAddress();
        if (StringUtils.isEmpty(addressFromForm)) {
            model.addAttribute("errorMessage", "error");
            return "send";
        }

        String[] addresses = addressFromForm.split(";");
        for(String address : addresses) {
            if (!Validator.isMailAddress(address)) {
                model.addAttribute("errorMessage", "error");
                return "send";
            }

            MailInfo mail = new MailInfo("gadget.mailsender@gmail.com", address, form.getSubject(), form.getBody());
            try {
                mailService.send(mail);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "error");
            }
        }

        return "send";
    }

}
