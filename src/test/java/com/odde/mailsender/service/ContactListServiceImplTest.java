package com.odde.mailsender.service;

import com.odde.mailsender.data.ContactList;
import com.odde.mailsender.data.Contact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactListServiceImplTest {

    @Autowired
    private ContactListService contactListService;

    @Before
    public void initFile(){
        File file = new File(ContactList.FILE_PATH);
        boolean isDelete = file.delete();
        System.out.println("file delete result is " + isDelete);
    }

    @Test
    public void addMailAddress() throws Exception {
        contactListService.add(new Contact("xxx@gmail.com"));
    }

    @Test
    public void getMailAddress() throws Exception {
        String mailAddress = "yyy@gmail.com";
        contactListService.add(new Contact(mailAddress));
        List<Contact> contacts = contactListService.get();
        assertThat(contacts.size(), is(1));

        Boolean isContains = false;

        for (Contact item : contacts) {
            if (item.getMailAddress().equals(mailAddress)) {
                isContains = true;
            }
        }
        assertThat(isContains, is(true));
    }

    @Test
    public void getWhenNoItem() {
        List<Contact> contacts = contactListService.get();
        assertThat(contacts.size(), is(0));
    }
}