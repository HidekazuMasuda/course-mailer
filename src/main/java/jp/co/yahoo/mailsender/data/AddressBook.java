package jp.co.yahoo.mailsender.data;

import java.util.ArrayList;
import java.util.List;

public class AddressBook {

    private List<AddressItem> addressItems = new ArrayList<AddressItem>();

    public boolean save() {
        return true;
    }

    public void add(AddressItem addressItem) {
        this.addressItems.add(addressItem);
    }
}
