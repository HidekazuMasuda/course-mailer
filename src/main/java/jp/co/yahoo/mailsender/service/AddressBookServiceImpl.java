package jp.co.yahoo.mailsender.service;

import jp.co.yahoo.mailsender.data.AddressBook;
import jp.co.yahoo.mailsender.data.AddressItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {


    private AddressBook addressBook = new AddressBook();

    @Override
    public void add(AddressItem addressItem) throws Exception {
        addressBook.add(addressItem);
    }

    @Override
    public List<AddressItem> get() {
        return addressBook.getAddressItems();
    }
}
