package jp.co.yahoo.mailsender.controller;

import jp.co.yahoo.mailsender.data.AddressItem;
import jp.co.yahoo.mailsender.service.AddressBookService;
import jp.co.yahoo.mailsender.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContactListControllerTest {

    @MockBean
    private AddressBookService addressBookService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getSize1ContactList() throws Exception {

        List<AddressItem> result = new ArrayList<>();
        result.add(new AddressItem("aaa@example.com"));

        when(addressBookService.get()).thenReturn(result);
        mvc.perform(get("/contact-list"))
                .andExpect(model().attribute("contactList", hasSize(1)));
    }

    @Test
    public void addEmailAddress() throws Exception {

        mvc.perform(post("/contact-list")
                .param("address", "aaa@yahoo.co.jp"))
                .andExpect(view().name("contact-list"));

        verify(addressBookService).add(argThat(mail -> mail.getMailAddress().equals("aaa@yahoo.co.jp")));
    }

}
