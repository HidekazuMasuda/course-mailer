package com.odde.mailsender.controller;

import com.odde.mailsender.data.AddressItem;
import com.odde.mailsender.form.ContactListForm;
import com.odde.mailsender.form.MailSendForm;
import com.odde.mailsender.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class ContactListController {

    private static final String CONTACT_LIST_VIEW = "contact-list";
    private static final String REDIRECT_CONTACT_LIST_VIEW = "redirect:/contact-list";
    private static final String SEND_VIEW = "send";

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/contact-list")
    public String getContactList(@ModelAttribute("form") ContactListForm form, Model model) {
        renderContractLists(model);
        return CONTACT_LIST_VIEW;
    }


    @PostMapping("/contact-list")
    public String addContactList(@Valid @ModelAttribute("form") ContactListForm form, BindingResult result, Model model) {
        if(result.hasErrors()) {
            renderContractLists(model);
            return CONTACT_LIST_VIEW;
        }

        try {
            addressBookService.add(new AddressItem(form.getAddress(), form.getName()));
        } catch (Exception e) {
            result.rejectValue("","", e.getMessage());
            return CONTACT_LIST_VIEW;
        }

        return REDIRECT_CONTACT_LIST_VIEW;
    }

    @PostMapping("/create-mail")
    public String createEmail(@RequestParam(required = false) String[] mailAddress, Model model) {
        model.addAttribute("form", MailSendForm.create(mailAddress));
        return SEND_VIEW;
    }

    private void renderContractLists(Model model) {
        model.addAttribute("contactList", addressBookService.get());
    }
}
