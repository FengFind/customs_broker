package com.zwu.customs_broker.bean;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * ID
     **/
    private Integer ID;
    /**
     * 用户名
     **/
    private String userName;
    /**
     * 密码
     **/
    private String passWord;
    /**
     * 公司
     **/
    private String company;
    /**
     * 海关编码10位
     **/
    private String agentCode;
    /**
     * QQ
     **/
    private String QQ;
    /**
     * QQ_Group
     **/
    private String QQgroup;

    private String regCode;
    /**
     * 操作员
     */
    private String name;

    private String fromAgentCode;
    /**
     * 用户权限
     **/
    private Integer authorization;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getQQgroup() {
        return QQgroup;
    }

    public void setQQgroup(String QQgroup) {
        this.QQgroup = QQgroup;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromAgentCode() {
        return fromAgentCode;
    }

    public void setFromAgentCode(String fromAgentCode) {
        this.fromAgentCode = fromAgentCode;
    }

    public Integer getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Integer authorization) {
        this.authorization = authorization;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", company='" + company + '\'' +
                ", agentCode='" + agentCode + '\'' +
                ", QQ='" + QQ + '\'' +
                ", QQgroup='" + QQgroup + '\'' +
                ", regCode='" + regCode + '\'' +
                ", name='" + name + '\'' +
                ", fromAgentCode='" + fromAgentCode + '\'' +
                ", authorization=" + authorization +
                '}';
    }
}