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
}