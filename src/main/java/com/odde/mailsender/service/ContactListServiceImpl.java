package com.odde.mailsender.service;

import com.odde.mailsender.data.Contact;
import com.odde.mailsender.data.ContactListImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactListServiceImpl implements ContactListService {


    private ContactListImpl contactList = new ContactListImpl();

    @Override
    public void add(Contact contact) throws Exception {
        contactList.load();
        contactList.add(contact);
        contactList.save();
    }

    @Override
    public List<Contact> get() {
        return contactList.findAll();
    }

    public Contact findBy(String address) {
        return contactList.findBy(address);
    }
}
