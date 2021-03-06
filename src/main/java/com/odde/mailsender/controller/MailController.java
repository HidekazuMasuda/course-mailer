package com.odde.mailsender.controller;

import com.odde.mailsender.data.Contact;
import com.odde.mailsender.form.MailSendForm;
import com.odde.mailsender.service.ContactListService;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MailController {

    private static final String SEND_VIEW = "send";
    private static final String REDIRECT_SEND_VIEW = "redirect:/send";

    @Autowired
    private MailService mailService;
    @Autowired
    private ContactListService contactListService;

    @GetMapping("/send")
    public String send(@ModelAttribute("form") MailSendForm form, BindingResult result, Model model) {
        return SEND_VIEW;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendEmail(@Valid @ModelAttribute("form") MailSendForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return SEND_VIEW;
        }

        try {
            if (!form.isTemplate()) {
                mailService.sendMultiple(Arrays.stream(form.getAddresses()).map(form::createNormalMail).collect(Collectors.toList()));
                return REDIRECT_SEND_VIEW;
            }

            List<MailInfo> mails = new ArrayList<>();
            for (String address : form.getAddresses()) {
                if (contactNameExists(contactListService.findBy(address)))
                    throw new Exception("When you use template, choose email from contract list that has a name");

                mails.add(form.createRenderedMail(contactListService.findBy(address)));
            }
            mailService.sendMultiple(mails);
            return REDIRECT_SEND_VIEW;
        } catch (Exception e) {
            result.rejectValue("", "", e.getMessage());
            return SEND_VIEW;
        }
    }

    private boolean contactNameExists(Contact contact) {
        return contact == null || StringUtils.isEmpty(contact.getName());
    }

}
