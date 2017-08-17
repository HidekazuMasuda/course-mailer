package jp.co.yahoo.mailsender.service;

import jp.co.yahoo.mailsender.data.AddressBook;
import jp.co.yahoo.mailsender.data.AddressItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {



    @Override
    public void add(AddressItem addressItem) {

    }

    @Override
    public List<AddressItem> get() {
        return null;
    }
}
