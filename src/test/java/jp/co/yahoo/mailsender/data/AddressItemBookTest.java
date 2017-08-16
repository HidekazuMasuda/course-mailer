package jp.co.yahoo.mailsender.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AddressItemBookTest {

    AddressBook addressBook = new AddressBook();

    @Before
    public void initFile(){
        String filePath = System.getenv("HOME") + "/gadget-mailsender/addressbook.json";
        File file = new File(filePath);
        file.delete();
    }

    @Test
    public void normalSave() throws IOException {

        AddressItem addressItem = new AddressItem("stanly@odd-e.com");
        this.addressBook.add(addressItem);

        assertThat(this.addressBook.save(), is(true));
    }

    @Test
    public void existFile() throws IOException {
        AddressItem addressItem = new AddressItem("stanly@odd-e.com");
        this.addressBook.add(addressItem);
        this.addressBook.save();

        String filePath = System.getenv("HOME") + "/gadget-mailsender/addressbook.json";
        File file = new File(filePath);
        assertThat(file.exists(), is(true));
    }

    @Test
    public void checkContent() throws IOException {

        AddressItem addressItem = new AddressItem("stanly@odd-e.com");
        this.addressBook.add(addressItem);
        this.addressBook.save();

        String filePath = System.getenv("HOME") + "/gadget-mailsender/addressbook.json";
        File file = new File(filePath);
        List<String> addressList = FileUtils.readLines(file, "utf-8");

        ObjectMapper mapper = new ObjectMapper();
        String addressItemAsString = mapper.writeValueAsString(addressItem);

        assertThat(addressList.size(), is(1));
        assertThat(addressList.get(0), is(addressItemAsString));
    }

    @Test
    public void loadOneItem() throws IOException {
        AddressItem addressItem = new AddressItem("stanly@odd-e.com");
        AddressBook addressBookA = new AddressBook();
        AddressBook addressBookB = new AddressBook();

        addressBookA.add(addressItem);
        addressBookA.save();
        addressBookB.load();

        assertThat(addressBookB.getAddressItems().size(), is(1));
        assertThat(addressBookB.getAddressItems().get(0).getMailAddress(), is(addressItem.getMailAddress()));
    }
}
