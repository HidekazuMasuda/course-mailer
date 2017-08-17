package jp.co.yahoo.mailsender.service;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import static org.hamcrest.core.Is.is;
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
    public void mailSendSuccess() throws Exception {
        service.send(new MailBuilder().aValidMail().build());

        // wiserが受信したデータをログ出力
        List<WiserMessage> messages = wiser.getMessages();

        assertThat(messages.size(), is(1));

        WiserMessage wiserMessage = messages.get(0);
        assertThat(wiserMessage.getEnvelopeReceiver(), is("gadget.mailsender@gmail.com"));
        assertThat(wiserMessage.getEnvelopeSender(), is("gadget.mailsender@gmail.com"));
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldEmpty() throws Exception {
        service.send(new MailBuilder().aValidMail().withFrom("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldEmpty() throws Exception {
        service.send(new MailBuilder().aValidMail().withTo("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldEmpty() throws Exception {
        service.send(new MailBuilder().aValidMail().withSubject("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldEmpty() throws Exception {
        service.send(new MailBuilder().aValidMail().withBody("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldNull() throws Exception {
        service.send(new MailBuilder().aValidMail().withFrom(null).build());
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldNull() throws Exception {
        service.send(new MailBuilder().aValidMail().withTo(null).build());
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldNull() throws Exception {
        service.send(new MailBuilder().aValidMail().withSubject(null).build());
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldNull() throws Exception {
        service.send(new MailBuilder().aValidMail().withBody(null).build());
    }

    public static class MailBuilder {
        MailInfo mail;

        public MailBuilder aValidMail() {
            mail = new MailInfo("gadget.mailsender@gmail.com", "gadget.mailsender@gmail.com", "subject", "some body.");
            return this;
        }

        public MailInfo build() {
            return mail;
        }

        public MailBuilder withFrom(String from) {
            mail.setFrom(from);
            return this;
        }

        public MailBuilder withTo(String to) {
            mail.setTo(to);
            return this;
        }

        public MailBuilder withSubject(String subject) {
            mail.setSubject(subject);
            return this;
        }

        public MailBuilder withBody(String body) {
            mail.setBody(body);
            return this;
        }
    }
}