package jp.co.yahoo.mailsender.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private static final String MAIL_HOST = "smtp.gmail.com";
    private static final String SSL_SMTP_PORT = "465";
    private static final String CHARSET = "ISO-2022-JP";
    private static final String SENDER_NAME = "gadget.mailsender";
    private static final String PASSWORD = "mailsender";

    @Override
    public void send(MailInfo mailInfo) throws Exception {

        if (mailInfo.getFrom() == null || mailInfo.getFrom().equals("")) {
            throw new Exception("from field is empty");
        } else if (mailInfo.getTo() == null || mailInfo.getTo().equals("")) {
            throw new Exception("to field is empty");
        } else if (mailInfo.getSubject() == null || mailInfo.getSubject().equals("")) {
            throw new Exception("subject field is empty");
        } else if (mailInfo.getBody() == null || mailInfo.getBody().equals("")) {
            throw new Exception("body field is empty");
        }

        SimpleEmail mailClient = new SimpleEmail();
        initMailClient(mailClient);
        buildMailInfo(mailInfo, mailClient);

        String value = mailClient.send();
        System.out.println(value);

    }

    private void buildMailInfo(MailInfo mailInfo, SimpleEmail mailClient) throws EmailException {
        mailClient.setFrom(mailInfo.getFrom());
        mailClient.setSubject(mailInfo.getSubject());
        mailClient.setMsg(mailInfo.getBody());
        mailClient.addTo(mailInfo.getTo());
    }

    private void initMailClient(SimpleEmail mailClient) {
        mailClient.setHostName(MAIL_HOST);
        mailClient.setSslSmtpPort(SSL_SMTP_PORT);
        mailClient.setSSL(true);
        mailClient.setCharset(CHARSET);
        mailClient.setAuthentication(SENDER_NAME, PASSWORD);

    }

}
