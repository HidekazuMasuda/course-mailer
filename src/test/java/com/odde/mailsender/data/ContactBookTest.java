package com.odde.mailsender.data;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ContactBookTest {

    ContactList contactList = new ContactList();
    Contact contact = new Contact("stanly@odd-e.com", "stanly");


    @Test
    public void addAddressItem() throws Exception {
        this.contactList.add(contact);
        assertThat(contactList.getContacts().get(0).getMailAddress(), is(contact.getMailAddress()));
    }

    @Test
    public void existFile() throws Exception {
        this.contactList.add(contact);
        this.contactList.save();
        assertThat(new File(ContactList.FILE_PATH).exists(), is(true));
    }

    @Test
    public void saveAddressItem() throws Exception {
        this.contactList.add(contact);
        this.contactList.save();

        List<String> addressList = FileUtils.readLines(new File(ContactList.FILE_PATH), "utf-8");

        assertThat(addressList.size(), is(1));
        assertThat(addressList.get(0), is(contact.addressItemToString()));
    }

    @Test
    public void loadOneItem() throws Exception {
        contactList.add(contact);
        contactList.save();

        contactList.load();

        assertThat(contactList.getContacts().size(), is(1));
        assertThat(contactList.getContacts().get(0).getMailAddress(), is(contact.getMailAddress()));
        assertThat(contactList.getContacts().get(0).getName(), is(contact.getName()));
    }

    @Test(expected = Exception.class)
    public void checkDuplication() throws Exception {

        contactList.add(contact);
        contactList.add(contact);
    }

    @Test
    public void findByAddress() throws Exception {
        Contact actual = contactList.findByAddress("stanly@odd-e.com");
        assertThat(actual.getName(), is(contact.getName()));
        assertThat(actual.getMailAddress(), is(contact.getMailAddress()));
    }
}
