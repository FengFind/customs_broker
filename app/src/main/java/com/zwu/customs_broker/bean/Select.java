package com.zwu.customs_broker.bean;

public class Select {
    private String[] contactsNameType;
    private String[] userCompanyType;
    private String[] contactsCompanyType;

    public String[] getContactsNameType() {
        return contactsNameType;
    }

    public void setContactsNameType(String[] contactsNameType) {
        this.contactsNameType = contactsNameType;
    }

    public String[] getUserCompanyType() {
        return userCompanyType;
    }

    public void setUserCompanyType(String[] userCompanyType) {
        this.userCompanyType = userCompanyType;
    }

    public String[] getContactsCompanyType() {
        return contactsCompanyType;
    }

    public void setContactsCompanyType(String[] contactsCompanyType) {
        this.contactsCompanyType = contactsCompanyType;
    }
}
