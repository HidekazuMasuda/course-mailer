package jp.co.yahoo.mailsender.service;

import java.util.List;

public interface MailService {
    public void send(MailInfo mailInfo) throws Exception;

    public void sendMultiple(List<MailInfo> mailInfoList) throws Exception;
}
