package jp.co.yahoo.mailsender.service;

import jp.co.yahoo.mailsender.data.AddressItem;
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

import java.util.ArrayList;
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


        List<MailInfo> mailInfoList = new ArrayList<>();
        mailInfoList.add(validMail().withTo("first@gmail.com").build());
        mailInfoList.add(validMail().withTo("second@gmail.com").build());
        service.sendMultiple(mailInfoList);

        List<WiserMessage> messages = wiser.getMessages();
        assertThat(messages.size(), is(2));

        assertThat(messages.get(0).getEnvelopeReceiver(), is(mailInfoList.get(0).getTo()));
        assertThat(messages.get(0).getEnvelopeSender(), is(mailInfoList.get(0).getFrom()));
        assertThat(messages.get(0).getMimeMessage().getSubject(), is(mailInfoList.get(0).getSubject()));
        assertThat(messages.get(0).getMimeMessage().getContent().toString().trim(), is(mailInfoList.get(0).getBody()));

        assertThat(messages.get(1).getEnvelopeReceiver(), is(mailInfoList.get(1).getTo()));
        assertThat(messages.get(1).getEnvelopeSender(), is(mailInfoList.get(1).getFrom()));
        assertThat(messages.get(1).getMimeMessage().getSubject(), is(mailInfoList.get(1).getSubject()));
        assertThat(messages.get(1).getMimeMessage().getContent().toString().trim(), is(mailInfoList.get(1).getBody()));
    }

}