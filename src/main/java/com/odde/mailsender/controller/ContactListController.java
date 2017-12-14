package com.odde.mailsender.controller;

import com.odde.mailsender.data.AddressItem;
import com.odde.mailsender.form.ContactListForm;
import com.odde.mailsender.form.MailSendForm;
import com.odde.mailsender.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ContactListController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/contact-list")
    public String getContactList(Model model) {

        List<AddressItem> addressList = addressBookService.get();
        model.addAttribute("contactList", addressList);
        return "contact-list";
    }

    @PostMapping("/contact-list")
    public String addContactList(@ModelAttribute ContactListForm form, Model model) {

        AddressItem input = new AddressItem(form.getAddress(), form.getName());
        try {
            addressBookService.add(input);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "error");
        }
        List<AddressItem> addressList = addressBookService.get();
        model.addAttribute("contactList", addressList);
        return "contact-list";
    }

    @PostMapping("/create-mail")
    public String createEmail(@RequestParam(required = false) String[] mailAddress, Model model) {

        model.addAttribute("address", joinMailAddress(mailAddress));

        return "send";
    }

    private String joinMailAddress(String[] mailAddress) {
        return mailAddress == null ? "" : String.join(";", mailAddress);
    }

}
