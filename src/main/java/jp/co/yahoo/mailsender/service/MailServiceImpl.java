package jp.co.yahoo.mailsender.service;

import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final String mailHost = "smtp.gmail.com";

    @Override
    public void send(String from, String to, String subject, String body) throws Exception {


        if (from == null || from.equals("")) {
            throw new Exception("from field is empty");
        } else if (to == null || to.equals("")) {
            throw new Exception("to field is empty");
        } else if (subject == null || subject.equals("")) {
            throw new Exception("subject field is empty");
        } else if (body == null || body.equals("")) {
            throw new Exception("body field is empty");
        }

        SimpleEmail email = new SimpleEmail();

        email.setHostName(mailHost);
        email.setSSL(true);
        email.setSslSmtpPort("465");
        email.setCharset("ISO-2022-JP");
        email.setAuthentication("gadget.mailsender", "mailsender");

        //送信内容の設定
        email.setFrom(from);
        email.setSubject(subject);
        email.setMsg(body);
        email.addTo(to);

        String value = email.send();
        System.out.println(value);

    }

}
