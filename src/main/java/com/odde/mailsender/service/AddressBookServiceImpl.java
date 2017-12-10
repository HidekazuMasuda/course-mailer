package com.odde.mailsender.service;

import com.odde.mailsender.data.AddressBook;
import com.odde.mailsender.data.AddressItem;
import com.odde.mailsender.utils.Validator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {


    private AddressBook addressBook  = new AddressBook();

    @Override
    public void add(AddressItem addressItem) throws Exception {
        addressBook.load();
        if(Validator.isMailAddress(addressItem.getMailAddress())){
            addressBook.add(addressItem);
            addressBook.save();
        }else{
            throw new Exception("mail address is empty");
        }
    }

    @Override
    public List<AddressItem> get() throws RuntimeException {
        try {
            addressBook.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addressBook.getAddressItems();
    }

    public AddressItem findByAddress(String address) throws Exception {
        return addressBook.findByAddress(address);
    }
}
