package com.odde.mailsender.controller;

import com.odde.mailsender.data.AddressItem;
import com.odde.mailsender.form.MailSendForm;
import com.odde.mailsender.service.AddressBookService;
import com.odde.mailsender.service.MailInfo;
import com.odde.mailsender.service.MailService;
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

@Controller
public class MailController {

    @Autowired
    private MailService mailService;
    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/send")
    public String send(@ModelAttribute("form") MailSendForm form, BindingResult result, Model model) {
        return "send";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendEmail(@Valid @ModelAttribute("form") MailSendForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "send";
        }

        String[] addresses = form.getAddress().split("\\s*;\\s*");

        try {
            validReplaceAttribute(addresses, form);
            mailService.sendMultiple(createMailInfoList(addresses, form));
        } catch (Exception e) {
            result.rejectValue("","", e.getMessage());
            return "send";
        }

        return "redirect:/send";
    }

    private List<MailInfo> createMailInfoList(String[] addresses, MailSendForm form) throws Exception {

        List<MailInfo> mailInfoList = new ArrayList<>();
        String subject = form.getSubject();
        String body = form.getBody();

        for (String address : addresses) {

            String replacedSubject = subject;
            String replacedBody = body;

            AddressItem addressItem = addressBookService.findByAddress(address);

            if (addressItem != null) {
                replacedSubject = StringUtils.replace(subject, "$name", addressItem.getName());
                replacedBody = StringUtils.replace(body, "$name", addressItem.getName());
            }

            MailInfo mail = new MailInfo("gadget.mailsender@gmail.com", address, replacedSubject, replacedBody);
            mailInfoList.add(mail);
        }

        return mailInfoList;
    }

    private void validReplaceAttribute(String[] addresses, MailSendForm form) throws Exception {
        if (StringUtils.contains(form.getSubject(), "$name") || StringUtils.contains(form.getBody(), "$name")) {
            for (String address : addresses) {

                AddressItem addressItem = addressBookService.findByAddress(address);

                if (addressItem == null || StringUtils.isEmpty(addressItem.getName())) {
                    throw new Exception("When you use template, choose email from contract list that has a name");
                }
            }
        }
    }
}
