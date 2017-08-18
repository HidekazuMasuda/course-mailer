package jp.co.yahoo.mailsender.controller;

import jp.co.yahoo.mailsender.data.AddressBook;
import jp.co.yahoo.mailsender.data.AddressItem;
import jp.co.yahoo.mailsender.service.MailInfo;
import jp.co.yahoo.mailsender.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static jp.co.yahoo.mailsender.service.MailBuilder.validMail;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MailControllerTest {

    @MockBean
    private MailService mailService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void sendToMultipleAddresses() throws Exception {
        String addresses = "abc@gmail.com;john@gmail.com";
        MailInfo mailInfo = validMail().withTo(addresses).build();

        getPerform(mailInfo).andExpect(view().name("send"));

        verify(mailService, times(1 )).sendMultiple(any());
    }

    @Test
    public void showErrorIfEmptyMailAddress() throws Exception {
        MailInfo mailInfo = validMail().withTo("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void showErrorIfEmptySubject() throws Exception {

        MailInfo mailInfo = validMail().withSubject("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void showErrorIfEmptyBody() throws Exception {

        MailInfo mailInfo = validMail().withBody("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void manyAddressWithInvalidAddress() throws Exception {

        MailInfo mailInfo = validMail().withTo("abcdefghi123@xxx.com ; xxx.com; stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));


        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void replaceSubjectSuccess() throws Exception {
        AddressBook addressBook = new AddressBook();
        AddressItem addressItem = new AddressItem("gadget.mailsender@gmail.com", "Aki");
        addressBook.add(addressItem);
        addressBook.save();

        MailInfo mailInfo = validMail().withSubject("Hello $name").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(0).getSubject().equals("Hello Aki")));
    }

    @Test
    public void sendMultipleWithSubjectReplaced() throws Exception {
        AddressBook addressBook = new AddressBook();
        AddressItem addressItem = new AddressItem("gadget.mailsender@gmail.com", "Aki");
        addressBook.add(addressItem);
        addressItem = new AddressItem("stanly@xxx.com", "Stanly");
        addressBook.add(addressItem);
        addressBook.save();

        MailInfo mailInfo = validMail().withSubject("Hello $name").withTo("gadget.mailsender@gmail.com;stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(0).getSubject().equals("Hello Aki")));
        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(1).getSubject().equals("Hello Stanly")));
    }

    

    private ResultActions getPerform(MailInfo mailInfo) throws Exception {
        return mvc.perform(post("/send")
                .param("from", mailInfo.getFrom())
                .param("address", mailInfo.getTo())
                .param("subject", mailInfo.getSubject())
                .param("body", mailInfo.getBody()));
    }

}
