package jp.co.yahoo.mailsender.utils;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ValidatorTest {

    @Test
    public void emptyMailAddress() throws Exception {
        String mailAddress = "";
        boolean expected = false;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void nullMailAddress() throws Exception {
        String mailAddress = null;
        boolean expected = false;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void mailAddressWithoutAtSymbol() throws Exception {
        String mailAddress = "stanly";
        boolean expected = false;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void mailAddressStartsWithAtSymbol() throws Exception {
        String mailAddress = "@stanly";
        boolean expected = false;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void mailAddressEndsWithAtSymbol() throws Exception {
        String mailAddress = "stanly@";
        boolean expected = false;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void mailAddressOnlyAtSymbol() throws Exception {
        String mailAddress = "@";
        boolean expected = false;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void mailAddressTwoAtSymbol() throws Exception {
        String mailAddress = "@@";
        boolean expected = false;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void normalMailAddress() throws Exception {
        String mailAddress = "stanly@odd-e.com";
        boolean expected = true;

        boolean actual = Validator.isMailAddress(mailAddress);

        assertThat(expected, is(actual));
    }

    @Test
    public void nullSubject() throws Exception {

        String subject = null;
        boolean expected = false;

        boolean actual = Validator.isSubject(subject);

        assertThat(expected, is(actual));
    }

    @Test
    public void emptySubject() throws Exception {

        String subject = "";
        boolean expected = false;

        boolean actual = Validator.isSubject(subject);

        assertThat(expected, is(actual));
    }

    @Test
    public void normalSubject() throws Exception {

        String subject = "subject";
        boolean expected = true;

        boolean actual = Validator.isSubject(subject);

        assertThat(expected, is(actual));
    }

    @Test
    public void nullBody() throws Exception {

        String body = null;
        boolean expected = false;

        boolean actual = Validator.isBody(body);

        assertThat(expected, is(actual));
    }

    @Test
    public void emptyBody() throws Exception {

        String body = "";
        boolean expected = false;

        boolean actual = Validator.isBody(body);

        assertThat(expected, is(actual));
    }

    @Test
    public void normalBody() throws Exception {

        String body = "body";
        boolean expected = true;

        boolean actual = Validator.isBody(body);

        assertThat(expected, is(actual));
    }
}