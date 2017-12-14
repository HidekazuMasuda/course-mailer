package com.odde.mailsender.form;

import org.hibernate.validator.constraints.NotEmpty;

public class ContactListForm {
    @NotEmpty
    private String address;
    private String name;


    public String getAddress() {
        return address;
    }
    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void setName(String name) {
        this.name = name;
    }
}
