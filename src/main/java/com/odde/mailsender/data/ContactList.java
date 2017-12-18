package com.odde.mailsender.data;

import java.io.IOException;
import java.util.List;

public interface ContactList {
    void add(Contact contact) throws Exception;

    boolean save() throws IOException;

    Contact findBy(String address);

    List<Contact> findAll();
}
