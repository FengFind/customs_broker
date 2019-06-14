package com.zwu.customs_broker.bean;

import java.io.Serializable;

public class Login implements Serializable {
    private String code;
    private User info;

    public Login(String code, User info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return info;
    }

    public void setUser(User info) {
        this.info = info;
    }
}
