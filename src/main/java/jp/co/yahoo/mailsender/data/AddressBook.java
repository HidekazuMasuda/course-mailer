package jp.co.yahoo.mailsender.data;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBook {

    private List<AddressItem> addressItems = new ArrayList<AddressItem>();
    public static final String FILE_PATH = System.getenv("HOME") + "/gadget-mailsender/addressbook.json";

    public void load() throws Exception {
        if (!addressItems.isEmpty()) {
            addressItems.clear();
        }

        List<String> addressList = FileUtils.readLines(new File(FILE_PATH), "utf-8");
        for ( String address : addressList ){
            add(AddressItem.convertJsonToObject(address));
        }
    }

    public void add(AddressItem addressItem) throws Exception {
        for(AddressItem item : this.addressItems){
            if(item.getMailAddress().equals(addressItem.getMailAddress())){
                throw new Exception("Duplicate mail address");
            }
        }
        this.addressItems.add(addressItem);
    }

    public boolean save() throws IOException {
        File file = new File(FILE_PATH);

        File directory = new File(file.getParent());
        directory.mkdirs();

        if(file.exists() == false && file.createNewFile() == false){
            return false;
        }

        try (BufferedWriter writer = getWriter(file)) {
            for (AddressItem addressItem : addressItems) {
                writer.write(addressItem.addressItemToString());
                writer.newLine();
            }
        }

        return true;
    }

    private BufferedWriter getWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
    }


    public List<AddressItem> getAddressItems() {
        return addressItems;
    }
}
