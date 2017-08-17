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
        mvc.perform(post("/send")
                .param("from", "gadget.mailsender@gmail.com")
                .param("address", "aki@gmail.com")
                .param("subject", "is a subject")
                .param("body", "is a body"))
                .andExpect(view().name("send"));

        verify(mailService).send(argThat(mail -> mail.getSubject().equals("is a subject")));
        verify(mailService).send(argThat(mail -> mail.getFrom().equals("gadget.mailsender@gmail.com")));
        verify(mailService).send(argThat(mail -> mail.getTo().equals("aki@gmail.com")));
        verify(mailService).send(argThat(mail -> mail.getBody().equals("is a body")));
    }

    @Test
    public void showErrorIfEmptyMailAddress() throws Exception {
        mvc.perform(post("/send")
                .param("from", "gadget.mailsender@gmail.com")
                .param("address", "")
                .param("subject", "is a subject")
                .param("body", "is a body"))
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).send(any());
    }

    @Test
    public void showErrorIfEmptySubject() throws Exception {
        mvc.perform(post("/send")
                .param("from", "gadget.mailsender@gmail.com")
                .param("address", "aki@gmail.com")
                .param("subject", "")
                .param("body", "is a body"))
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).send(any());
    }

    @Test
    public void showErrorIfEmptyBody() throws Exception {
        mvc.perform(post("/send")
                .param("from", "gadget.mailsender@gmail.com")
                .param("address", "aki@gmail.com")
                .param("subject", "is a subject")
                .param("body", ""))
                .andExpect(model().attribute("errorMessage", "error"))
                .andExpect(view().name("send"));

        verify(mailService, never()).send(any());
    }

    @Test
    public void manyAddress() throws Exception {
        mvc.perform(post("/send")
                .param("from", "gadget.mailsender@gmail.com")
                .param("address", "abcdefghi123@xxx.com;stanly@xxx.com")
                .param("subject", "is a subject")
                .param("body", "hello. this is body"))
                .andExpect(view().name("send"));

        verify(mailService, times(2)).send(any());
    }

}
