package com.odde.mailsender.service;

import com.odde.mailsender.data.Contact;

import java.util.List;

public interface ContactListService {


    void add(Contact contact) throws Exception;

    List<Contact> get();

    Contact findBy(String address);
}
