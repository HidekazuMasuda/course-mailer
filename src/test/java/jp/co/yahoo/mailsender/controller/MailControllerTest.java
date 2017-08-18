package jp.co.yahoo.mailsender.controller;

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
    public void sendSuccess() throws Exception {
        MailInfo mailInfo = validMail().build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService).send(argThat(mail -> mail.getSubject().equals(mailInfo.getSubject())));
        verify(mailService).send(argThat(mail -> mail.getFrom().equals(mailInfo.getFrom())));
        verify(mailService).send(argThat(mail -> mail.getTo().equals(mailInfo.getTo())));
        verify(mailService).send(argThat(mail -> mail.getBody().equals(mailInfo.getBody())));
    }

    @Test
    public void showErrorIfEmptyMailAddress() throws Exception {
        MailInfo mailInfo = validMail().withTo("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).send(any());
    }

    @Test
    public void showErrorIfEmptySubject() throws Exception {

        MailInfo mailInfo = validMail().withSubject("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).send(any());
    }

    @Test
    public void showErrorIfEmptyBody() throws Exception {

        MailInfo mailInfo = validMail().withBody("").build();

        getPerform(mailInfo)
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).send(any());
    }

    @Test
    public void manyAddress() throws Exception {

        MailInfo mailInfo = validMail().withTo("abcdefghi123@xxx.com;stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService, times(2)).send(any());
    }

    @Test
    public void manyAddressWithSpace() throws Exception {

        MailInfo mailInfo = validMail().withTo("abcdefghi123@xxx.com ; stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));

        verify(mailService, times(2)).send(any());
    }

    @Test
    public void manyAddressWithInvalidAddress() throws Exception {

        MailInfo mailInfo = validMail().withTo("abcdefghi123@xxx.com ; xxx.com; stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("send"));


        verify(mailService, never()).send(any());
    }

    private ResultActions getPerform(MailInfo mailInfo) throws Exception {
        return mvc.perform(post("/send")
                .param("from", mailInfo.getFrom())
                .param("address", mailInfo.getTo())
                .param("subject", mailInfo.getSubject())
                .param("body", mailInfo.getBody()));
    }

}
