package com.odde.mailsender.controller;

import com.odde.mailsender.data.AddressItem;
import com.odde.mailsender.form.MailSendForm;
import com.odde.mailsender.service.AddressBookService;
import com.odde.mailsender.service.MailInfo;
import com.odde.mailsender.service.MailService;
import com.odde.mailsender.utils.Validator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;
    @Autowired
    private AddressBookService addressBookService;

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

        try {


            List<MailInfo> mailInfoList = new ArrayList<>();

            String subject = form.getSubject();
            String body = form.getBody();
            for (String address : addresses) {
                AddressItem addressItem = addressBookService.findByAddress(address);
                String replacedSubject = subject;
                String replacedBody = body;
                if (addressItem != null) {
                    if (StringUtils.isEmpty(addressItem.getName()) && StringUtils.contains(subject, "$name")) {
                        throw new Exception("name attribute is empty!!");
                    }
                    replacedSubject = StringUtils.replace(subject, "$name", addressItem.getName());
                    replacedBody = StringUtils.replace(body, "$name", addressItem.getName());
                }else {
                    if (StringUtils.contains(subject, "$name") || StringUtils.contains(body, "$name")){
                        throw new Exception("email address is not registered");
                    }
                }

                MailInfo mail = new MailInfo("gadget.mailsender@gmail.com", address, replacedSubject, replacedBody);
                mailInfoList.add(mail);
            }

            mailService.sendMultiple(mailInfoList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "error");
            return "send";
        }

        return "send";
    }

}
