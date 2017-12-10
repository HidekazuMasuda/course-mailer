package com.odde.mailsender.utils;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ValidatorTest {

    @Test
    public void emptyMailAddress() throws Exception {
        assertThat(Validator.isMailAddress(""), is(false) );
    }

    @Test
    public void nullMailAddress() throws Exception {
        assertThat(Validator.isMailAddress(null), is(false));
    }

    @Test
    public void mailAddressWithoutAtSymbol() throws Exception {
        assertThat(Validator.isMailAddress("stanly"), is(false));
    }

    @Test
    public void mailAddressStartsWithAtSymbol() throws Exception {
        assertThat(Validator.isMailAddress("@stanly"), is(false));
    }

    @Test
    public void mailAddressEndsWithAtSymbol() throws Exception {
        assertThat(Validator.isMailAddress("stanly@"), is(false));
    }

    @Test
    public void mailAddressOnlyAtSymbol() throws Exception {
        assertThat(Validator.isMailAddress("@"), is(false));
    }

    @Test
    public void mailAddressTwoAtSymbol() throws Exception {
        assertThat(Validator.isMailAddress("@@"), is(false));
    }

    @Test
    public void normalMailAddress() throws Exception {

        assertThat(Validator.isMailAddress("stanly@odd-e.com"), is(true));
    }

    @Test
    public void nullSubject() throws Exception {
        assertThat(Validator.isNotEmpty(null), is(false));
    }

    @Test
    public void emptySubject() throws Exception {
        assertThat(Validator.isNotEmpty(""), is(false));
    }

    @Test
    public void normalSubject() throws Exception {
        assertThat(Validator.isNotEmpty("subject"), is(true));
    }


}