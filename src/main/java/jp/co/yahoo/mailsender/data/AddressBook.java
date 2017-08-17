package jp.co.yahoo.mailsender.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBook {

    private List<AddressItem> addressItems = new ArrayList<AddressItem>();

    public boolean save() throws IOException {
        String filePath = System.getenv("HOME") + "/gadget-mailsender/addressbook.json";
        File file = new File(filePath);

        File directory = new File(file.getParent());
        directory.mkdirs();

        if(file.exists() == false && file.createNewFile() == false){
            return false;
        }

        try (BufferedWriter writer = getWriter(file)) {
            for (AddressItem addressItem : addressItems) {
                ObjectMapper mapper = new ObjectMapper();
                String addressItemAsString = mapper.writeValueAsString(addressItem);
                writer.write(addressItemAsString);
                writer.newLine();
            }
        }

        return true;
    }

    private BufferedWriter getWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
        return new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file, true), "UTF-8"));
    }

    public void add(AddressItem addressItem) {
        this.addressItems.add(addressItem);
    }

    public void load() throws IOException {
        String filePath = System.getenv("HOME") + "/gadget-mailsender/addressbook.json";
        File file = new File(filePath);
        List<String> addressList = FileUtils.readLines(file, "utf-8");

        ObjectMapper mapper = new ObjectMapper();

        for ( String address : addressList ){
            AddressItem addressItem = mapper.readValue(address, AddressItem.class);
            add(addressItem);
        }
    }

    public List<AddressItem> getAddressItems() {
        return addressItems;
    }
}
