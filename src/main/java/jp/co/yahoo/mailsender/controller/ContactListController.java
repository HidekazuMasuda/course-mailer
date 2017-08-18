package jp.co.yahoo.mailsender.controller;

import jp.co.yahoo.mailsender.data.AddressItem;
import jp.co.yahoo.mailsender.form.ContactListForm;
import jp.co.yahoo.mailsender.form.MailSendForm;
import jp.co.yahoo.mailsender.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

}
