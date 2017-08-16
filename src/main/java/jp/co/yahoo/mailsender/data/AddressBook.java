package jp.co.yahoo.mailsender.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import javax.naming.Name;
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

        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            for (AddressItem addressItem : addressItems) {
                ObjectMapper mapper = new ObjectMapper();
                String addressItemAsString = mapper.writeValueAsString(addressItem);
                bufferedWriter.write(addressItemAsString);
                bufferedWriter.newLine();
            }
        }

        return true;
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
