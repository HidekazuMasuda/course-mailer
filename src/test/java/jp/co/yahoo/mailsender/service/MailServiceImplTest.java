package jp.co.yahoo.mailsender.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import static jp.co.yahoo.mailsender.service.MailBuilder.validMail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceImplTest {

    @Autowired
    private MailService service;

    private Wiser wiser;

    @Before
    public void before() {
        wiser = new Wiser();
        wiser.setPort(2500);
        wiser.setHostname("localhost");
        wiser.start();
    }

    @After
    public void after() {
        wiser.stop();
    }

    @Test
    public void sendMultiple() throws Exception {
        service.sendMultiple(new String[]{"first@gmail.com", "second@gmail.com"}, "a subject", "a body");

        List<WiserMessage> messages = wiser.getMessages();
        assertThat(messages.size(), is(2));

        assertThat(messages.get(0).getEnvelopeReceiver(), is("first@gmail.com"));
        assertThat(messages.get(0).getEnvelopeSender(), is("gadget.mailsender@gmail.com"));
        assertThat(messages.get(0).getMimeMessage().getSubject(), is("a subject"));
        assertThat(messages.get(0).getMimeMessage().getContent().toString().trim(), is("a body"));

        assertThat(messages.get(1).getEnvelopeReceiver(), is("second@gmail.com"));
        assertThat(messages.get(1).getEnvelopeSender(), is("gadget.mailsender@gmail.com"));
        assertThat(messages.get(1).getMimeMessage().getSubject(), is("a subject"));
        assertThat(messages.get(1).getMimeMessage().getContent().toString().trim(), is("a body"));
    }

}