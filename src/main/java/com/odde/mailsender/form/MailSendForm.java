package com.odde.mailsender.form;

import com.odde.mailsender.data.Contact;
import com.odde.mailsender.service.MailInfo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class MailSendForm {
    public static final String COURSE_MAILER_GMAIL_COM = "course.mailer@gmail.com";
    public static final String TEMPLATE_STRING_NAME = "$name";

    @NotEmpty
    @Pattern(regexp = "^([_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})" + "(?:;" + "[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})" + ")*)?$", message = "{error.invalid.email}")
    private String address;
    @NotEmpty
    private String subject;
    @NotEmpty
    private String body;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String[] getAddresses() {
        return getAddress().split("\\s*;\\s*");
    }

    private String renderSubjectTemplate(Contact contact) {
        return StringUtils.replace(getSubject(), TEMPLATE_STRING_NAME, contact.getName());
    }

    private String renderBodyTemplate(Contact contact) {
        return StringUtils.replace(getBody(), TEMPLATE_STRING_NAME, contact.getName());
    }

    public static MailSendForm create(String[] addresses) {
        MailSendForm mailSendForm = new MailSendForm();
        mailSendForm.setAddress(joinMailAddress(addresses));

        return mailSendForm;
    }

    private static  String joinMailAddress(String[] mailAddress) {
        return mailAddress == null ? "" : String.join(";", mailAddress);
    }

    public boolean isTemplate() {
        return StringUtils.contains(getSubject(), TEMPLATE_STRING_NAME) || StringUtils.contains(getBody(), TEMPLATE_STRING_NAME);
    }

    public MailInfo createRenderedMail(Contact contact) {
        return new MailInfo(COURSE_MAILER_GMAIL_COM, contact.getMailAddress(), renderSubjectTemplate(contact), renderBodyTemplate(contact));
    }

    public MailInfo createNormalMail(String address) {
        return new MailInfo(COURSE_MAILER_GMAIL_COM, address, getSubject(), getBody());
    }
}
