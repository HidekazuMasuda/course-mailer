package jp.co.yahoo.mailsender.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class MailServiceTest {

    @Test
    public void mailSendSuccess() throws Exception {
        MailService service = new MailService();
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldEmpty() throws Exception {
        MailService service = new MailService();
        service.send("", "gadget.mailsender@gmail.com", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldEmpty() throws Exception {
        MailService service = new MailService();
        service.send("gadget.mailsender@gmail.com", "", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldEmpty() throws Exception {
        MailService service = new MailService();
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldEmpty() throws Exception {
        MailService service = new MailService();
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "subject", "");
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldNull() throws Exception {
        MailService service = new MailService();
        service.send(null, "gadget.mailsender@gmail.com", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldNull() throws Exception {
        MailService service = new MailService();
        service.send("gadget.mailsender@gmail.com", null, "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldNull() throws Exception {
        MailService service = new MailService();
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", null, "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldNull() throws Exception {
        MailService service = new MailService();
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "subject", null);
    }

}