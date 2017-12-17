package com.odde.mailsender.service;

import com.odde.mailsender.data.ContactList;
import com.odde.mailsender.data.Contact;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactListServiceImpl implements ContactListService {


    private ContactList contactList = new ContactList();

    @Override
    public void add(Contact contact) throws Exception {
        contactList.load();
        contactList.add(contact);
        contactList.save();
    }

    @Override
    public List<Contact> get() {
        return contactList.getContacts();
    }

    public Contact findBy(String address) {
        return contactList.findByAddress(address);
    }
}
