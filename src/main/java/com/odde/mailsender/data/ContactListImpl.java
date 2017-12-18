package com.odde.mailsender.data;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactListImpl implements ContactList {

    private List<Contact> contacts = new ArrayList<>();
    public static final String FILE_PATH = System.getenv("HOME") + "/course-mailer/addressbook.json";

    public void load() {
        clearContactList();

        readContactListFile().forEach(address -> {
            try {
                add(Contact.convertJsonToObject(address));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void add(Contact contact) throws Exception {
        for (Contact item : this.contacts) {
            if (item.getMailAddress().equals(contact.getMailAddress())) {
                throw new Exception("Duplicate address");
            }
        }
        this.contacts.add(contact);
    }

    @Override
    public boolean save() throws IOException {
        File file = new File(FILE_PATH);

        File directory = new File(file.getParent());
        directory.mkdirs();

        if (!file.exists() && !file.createNewFile()) {
            return false;
        }

        try (BufferedWriter writer = getWriter(file)) {
            for (Contact contact : contacts) {
                writer.write(contact.addressItemToString());
                writer.newLine();
            }

            contacts.clear();
        }

        return true;
    }

    @Override
    public Contact findBy(String address) {
        load();
        for (Contact contact : contacts) {
            if (contact.getMailAddress().equals(address)) {
                return contact;
            }
        }
        return null;
    }

    @Override
    public List<Contact> findAll() {
        load();
        return contacts;
    }

    private void clearContactList() {
        if (!contacts.isEmpty()) {
            contacts.clear();
        }
    }

    private List<String> readContactListFile() {
        List<String> addressList = new ArrayList<>();
        try {
            addressList = FileUtils.readLines(new File(FILE_PATH), "utf-8");
        } catch (FileNotFoundException e) {
            System.err.println("WARN: File not found. " + FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("WARN: File read error. " + FILE_PATH, e);
        }
        return addressList;
    }

    private BufferedWriter getWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    }

}
