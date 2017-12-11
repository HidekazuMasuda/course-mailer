package com.odde.mailsender.data;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBook {

    private List<AddressItem> addressItems = new ArrayList<AddressItem>();
    public static final String FILE_PATH = System.getenv("HOME") + "/course-mailer/addressbook.json";

    public void load() throws Exception {
        if (!addressItems.isEmpty()) {
            addressItems.clear();
        }

        try {
            List<String> addressList = FileUtils.readLines(new File(FILE_PATH), "utf-8");
            for (String address : addressList) {
                add(AddressItem.convertJsonToObject(address));
            }
        } catch (FileNotFoundException e) {
            System.err.println("WARN: File not found. " + FILE_PATH);
        }
    }

    public void add(AddressItem addressItem) throws Exception {
        for (AddressItem item : this.addressItems) {
            if (item.getMailAddress().equals(addressItem.getMailAddress())) {
                throw new Exception("Duplicate mail address");
            }
        }
        this.addressItems.add(addressItem);
    }

    public boolean save() throws IOException {
        File file = new File(FILE_PATH);

        File directory = new File(file.getParent());
        directory.mkdirs();

        if (file.exists() == false && file.createNewFile() == false) {
            return false;
        }

        try (BufferedWriter writer = getWriter(file)) {
            for (AddressItem addressItem : addressItems) {
                writer.write(addressItem.addressItemToString());
                writer.newLine();
            }

            addressItems.clear();
        }

        return true;
    }

    public AddressItem findByAddress(String address) throws Exception {
        load();
        for (AddressItem addressItem : addressItems) {
            if (addressItem.getMailAddress().equals(address)) {
                return addressItem;
            }
        }
        return null;
    }

    private BufferedWriter getWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    }

    public List<AddressItem> getAddressItems() {
        return addressItems;
    }
}
