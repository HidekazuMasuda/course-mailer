package jp.co.yahoo.mailsender.service;

public interface MailService {
    void send(MailInfo mailInfo) throws Exception;
}
