package jp.co.yahoo.mailsender.service;

public interface MailService {
    void send(String from, String to, String subject, String body) throws Exception;
}
