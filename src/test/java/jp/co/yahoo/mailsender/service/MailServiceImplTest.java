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
        service.send(validMail().build());

        // wiserが受信したデータをログ出力
        List<WiserMessage> messages = wiser.getMessages();

        assertThat(messages.size(), is(1));

        WiserMessage wiserMessage = messages.get(0);
        assertThat(wiserMessage.getEnvelopeReceiver(), is("gadget.mailsender@gmail.com"));
        assertThat(wiserMessage.getEnvelopeSender(), is("gadget.mailsender@gmail.com"));
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldEmpty() throws Exception {
        service.send(validMail().withFrom("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldEmpty() throws Exception {
        service.send(validMail().withTo("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldEmpty() throws Exception {
        service.send(validMail().withSubject("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldEmpty() throws Exception {
        service.send(validMail().withBody("").build());
    }

    @Test(expected = Exception.class)
    public void sendWithFromFieldNull() throws Exception {
        service.send(validMail().withFrom(null).build());
    }

    @Test(expected = Exception.class)
    public void sendWithToFieldNull() throws Exception {
        service.send(validMail().withTo(null).build());
    }

    @Test(expected = Exception.class)
    public void sendWithSubjectFieldNull() throws Exception {
        service.send(validMail().withSubject(null).build());
    }

    @Test(expected = Exception.class)
    public void sendWithBodyFieldNull() throws Exception {
        service.send(validMail().withBody(null).build());
    }

}