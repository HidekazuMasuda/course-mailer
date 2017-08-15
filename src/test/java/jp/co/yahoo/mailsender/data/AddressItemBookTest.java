package jp.co.yahoo.mailsender.data;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressItemBookTest {

    AddressBook addressBook = new AddressBook();

    @Test
    public void X() {

        AddressItem addressItem = new AddressItem("stanly@odd-e.com");
        this.addressBook.add(addressItem);
        boolean actual = this.addressBook.save();

        assertThat(true, is(actual));
    }
}
