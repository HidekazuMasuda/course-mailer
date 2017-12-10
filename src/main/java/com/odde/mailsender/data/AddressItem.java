package com.odde.mailsender.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

public class AddressItem implements Serializable {

    private String mailAddress;

    private String name;

    public AddressItem(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    @JsonCreator
    public AddressItem(@JsonProperty("mailAddress") String mailAddress, @JsonProperty("name") String name) {
        this.mailAddress = mailAddress;
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String addressItemToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static AddressItem convertJsonToObject(String address) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(address, AddressItem.class);
    }


}
