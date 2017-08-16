package jp.co.yahoo.mailsender.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceImplTest {

    @Autowired
    private MailService service;

    @Test
    public void mailSendSuccess() throws Exception {
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldEmpty() throws Exception {
        service.send("", "gadget.mailsender@gmail.com", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldEmpty() throws Exception {
        service.send("gadget.mailsender@gmail.com", "", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldEmpty() throws Exception {
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldEmpty() throws Exception {
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "subject", "");
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldNull() throws Exception {
        service.send(null, "gadget.mailsender@gmail.com", "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldNull() throws Exception {
        service.send("gadget.mailsender@gmail.com", null, "subject", "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldNull() throws Exception {
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", null, "some body.");
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldNull() throws Exception {
        service.send("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "subject", null);
    }

}