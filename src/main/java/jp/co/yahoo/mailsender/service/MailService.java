package jp.co.yahoo.mailsender.service;

public interface MailService {
    public void send(MailInfo mailInfo) throws Exception;

    public void sendMultiple(String[] addresses, String subject, String body) throws Exception;
}
