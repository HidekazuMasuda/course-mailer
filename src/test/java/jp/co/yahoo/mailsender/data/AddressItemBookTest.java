package jp.co.yahoo.mailsender.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressItemBookTest {

    AddressBook addressBook = new AddressBook();
    AddressItem addressItem = new AddressItem("stanly@odd-e.com");

    @Before
    public void initFile(){
        File file = new File(AddressBook.FILE_PATH);
        file.delete();
    }

    @Test
    public void normalSave() throws Exception {
        this.addressBook.add(addressItem);
        assertThat(this.addressBook.save(), is(true));
    }

    @Test
    public void existFile() throws Exception {
        this.addressBook.add(addressItem);
        this.addressBook.save();

        assertThat(new File(AddressBook.FILE_PATH).exists(), is(true));
    }

    @Test
    public void checkContent() throws Exception {

        this.addressBook.add(addressItem);
        this.addressBook.save();

        File file = new File(AddressBook.FILE_PATH);
        List<String> addressList = FileUtils.readLines(file, "utf-8");

        ObjectMapper mapper = new ObjectMapper();
        String addressItemAsString = mapper.writeValueAsString(addressItem);

        assertThat(addressList.size(), is(1));
        assertThat(addressList.get(0), is(addressItemAsString));
    }

    @Test
    public void loadOneItem() throws Exception {
        addressBook.add(addressItem);
        addressBook.save();

        addressBook.load();

        assertThat(addressBook.getAddressItems().size(), is(1));
        assertThat(addressBook.getAddressItems().get(0).getMailAddress(), is(addressItem.getMailAddress()));
    }

    @Test(expected = Exception.class)
    public void checkDuplication() throws Exception {

        addressBook.add(addressItem);
        addressBook.add(addressItem);
    }
}
