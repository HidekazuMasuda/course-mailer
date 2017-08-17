package jp.co.yahoo.mailsender.service;

import jp.co.yahoo.mailsender.data.AddressBook;
import jp.co.yahoo.mailsender.data.AddressItem;
import jp.co.yahoo.mailsender.utils.Validator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {


    private AddressBook addressBook = new AddressBook();

    @Override
    public void add(AddressItem addressItem) throws Exception {
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
}
