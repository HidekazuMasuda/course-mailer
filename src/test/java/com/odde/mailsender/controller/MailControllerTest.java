package com.odde.mailsender.controller;

import com.odde.mailsender.data.AddressBook;
import com.odde.mailsender.service.AddressBookService;
import com.odde.mailsender.service.MailBuilder;
import com.odde.mailsender.service.MailInfo;
import com.odde.mailsender.service.MailService;
import com.odde.mailsender.data.AddressItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private AddressBookService addressBookService;

    @Before
    public void setUp() {
        File file = new File(AddressBook.FILE_PATH);
        boolean isDelete = file.delete();
    }

    @Test
    public void sendToMultipleAddresses() throws Exception {
        String addresses = "abc@gmail.com;john@gmail.com";
        MailInfo mailInfo = MailBuilder.validMail().withTo(addresses).build();

        getPerform(mailInfo).andExpect(view().name("send"));

        verify(mailService, times(1 )).sendMultiple(any());
    }

    @Test
    public void showErrorIfEmptyMailAddress() throws Exception {
        MailInfo mailInfo = MailBuilder.validMail().withTo("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void showErrorIfEmptySubject() throws Exception {

        MailInfo mailInfo = MailBuilder.validMail().withSubject("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void showErrorIfEmptyBody() throws Exception {

        MailInfo mailInfo = MailBuilder.validMail().withBody("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void manyAddressWithInvalidAddress() throws Exception {

        MailInfo mailInfo = MailBuilder.validMail().withTo("abcdefghi123@xxx.com ; xxx.com; stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));


        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void sendMultipleWithSubjectAndBodyReplaced() throws Exception {

        addressBookService.add(new AddressItem("gadget.mailsender@gmail.com", "Aki"));
        addressBookService.add(new AddressItem("stanly@xxx.com", "Stanly"));

        MailInfo mailInfo = MailBuilder.validMail().withSubject("Hello $name").withBody("Hi $name").withTo("gadget.mailsender@gmail.com;stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(0).getSubject().equals("Hello Aki")));
        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(1).getSubject().equals("Hello Stanly")));
        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(0).getBody().equals("Hi Aki")));
        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(1).getBody().equals("Hi Stanly")));
    }

    @Test
    public void notSubjectReplaceWhenNotRegisteredAddress() throws Exception {
        MailInfo mailInfo = MailBuilder.validMail().withSubject("Hello $name").withTo("foobar@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void notBodyReplaceWhenNotRegisteredAddress() throws Exception {
        MailInfo mailInfo = MailBuilder.validMail().withBody("Hi $name").withTo("foobar@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void notSubjectReplaceWhenNoNameAddress() throws Exception {

        addressBookService.add(new AddressItem("gadget.mailsender@gmail.com", ""));

        MailInfo mailInfo = MailBuilder.validMail().withSubject("Hi $name").withTo("gadget.mailsender@gmail.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void notBodyReplaceWhenNoNameAddress() throws Exception {

        addressBookService.add(new AddressItem("gadget.mailsender@gmail.com", ""));

        MailInfo mailInfo = MailBuilder.validMail().withBody("Hi $name").withTo("gadget.mailsender@gmail.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService, never()).sendMultiple(any());
    }

    private ResultActions getPerform(MailInfo mailInfo) throws Exception {
        return mvc.perform(post("/send")
                .param("from", mailInfo.getFrom())
                .param("address", mailInfo.getTo())
                .param("subject", mailInfo.getSubject())
                .param("body", mailInfo.getBody()));
    }

}
