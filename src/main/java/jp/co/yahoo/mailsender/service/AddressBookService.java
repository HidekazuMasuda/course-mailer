package jp.co.yahoo.mailsender.service;

import jp.co.yahoo.mailsender.data.AddressItem;

import java.util.List;

public interface AddressBookService {


    void add(AddressItem addressItem) throws Exception;

    List<AddressItem> get() throws RuntimeException;

    AddressItem findByAddress(String address) throws Exception;
}
