package jp.co.yahoo.mailsender.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Value("${smtp.host}")
    private String mailHost;
    @Value("${smtp.port}")
    private Integer smtpPort;
    @Value("${smtp.ssl.port}")
    private String sslSmtpPort;
    @Value("${smtp.ssl.enable}")
    private Boolean sslEnable;
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


        mailClient.setHostName(mailHost);

        if (sslEnable) {
            mailClient.setSslSmtpPort(sslSmtpPort);
            mailClient.setSSL(true);
        } else {
            mailClient.setSmtpPort(smtpPort);
        }

        mailClient.setCharset(CHARSET);
        mailClient.setAuthentication(SENDER_NAME, PASSWORD);

    }

}
