package com.zwu.customs_broker.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 报关单
 */
public class CusDec implements Serializable {
    public CusDec() {
    }

    public CusDec(Integer ID, String entryId, String updateTime, String consignorCname , String billNo, String trafName, String contactsName, String contactsCompany, String cusDecStatus, String company) {
        this.ID = ID;
        this.entryId = entryId;
        this.updateTime = updateTime;
        this.consignorCname = consignorCname;
        this.billNo = billNo;
        this.trafName = trafName;
        this.contactsName = contactsName;
        this.contactsCompany = contactsCompany;
        this.cusDecStatus = cusDecStatus;
        this.company = company;
    }

    /**
     * 序号
     **/
    private Integer ID;
    /**
     * 关联号
     **/
    private String cusCiqNo;
    /**
     * 报关单号
     **/
    private String entryId;
    /**
     * 申报时间/最后更新时间
     **/
    private String updateTime;
    /**
     * 收发货人
     **/
    private String consignorCname;
    /**
     * 提单号
     **/
    private String billNo;
    /**
     * 插入时间
     **/
    private Date AddTime;
    /**
     * 商品数量
     **/
    private String goodsNum;
    /**
     * 生产销售单位
     **/
    private String ownerName;
    /**
     * 船名航次
     **/
    private String trafName;
    /**
     * 原始数据
     **/
    private String oriData;
    /**
     * 发送已申报消息
     **/
    private Date sendmsg_Delare;
    /**
     * 发送海关查验消息
     **/
    private Date sendmsg_Check;
    /**
     * 发送海关放行消息
     **/
    private Date sendmsg_Clearance;
    /**
     * 合同号
     **/
    private String contrNo;
    /**
     * 申报地海关
     **/
    private String customMasterName;
    /**
     * 联系人-操作员
     **/
    private String contactsName;
    /**
     * 联系人-公司
     **/
    private String contactsCompany;
    /**
     * 备注
     **/
    private String reMark;
    /**
     * 是否被查验,1为查验,0或null未查
     **/
    private Integer isChecked;
    /**
     * 海关是否放行,1为放行,0或null未放
     **/
    private Integer isClearanced;
    /**
     * 报关状态
     **/
    private String cusDecStatus;
    /**
     * 发送已录入消息
     **/
    private Date sendmsg_record;
    /**
     * 海关10位编码
     **/
    private String FromAgentCode;
    /**
     * 报关行
     **/
    private String company;
    /**
     * 认领日期
     **/
    private Date claimTime;
    /**
     * 开始更新日期
     **/
    private String fromTime;
    /**
     * 结束更新日期
     **/
    private String toTime;
    /**
     * 排序根据属性
     **/
    private String sortName;
    /**
     * 排序方式（正序、倒序）
     **/
    private String sortFormat;
    /**
     * 点击次数
     **/
    private Integer clicks;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTrafName() {
        return trafName;
    }

    public void setTrafName(String trafName) {
        this.trafName = trafName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getConsignorCname() {
        return consignorCname;
    }

    public void setConsignorCname(String consignorCname) {
        this.consignorCname = consignorCname;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public Integer getIsClearanced() {
        return isClearanced;
    }

    public void setIsClearanced(Integer isClearanced) {
        this.isClearanced = isClearanced;
    }

    public String getFromAgentCode() {
        return FromAgentCode;
    }

    public void setFromAgentCode(String fromAgentCode) {
        FromAgentCode = fromAgentCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    public String getCusDecStatus() {
        return cusDecStatus;
    }

    public void setCusDecStatus(String cusDecStatus) {
        this.cusDecStatus = cusDecStatus;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsCompany() {
        return contactsCompany;
    }

    public void setContactsCompany(String contactsCompany) {
        this.contactsCompany = contactsCompany;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortFormat() {
        return sortFormat;
    }

    public void setSortFormat(String sortFormat) {
        this.sortFormat = sortFormat;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    @Override
    public String toString() {
        return "CusDec{" +
                "ID=" + ID +
                ", cusCiqNo='" + cusCiqNo + '\'' +
                ", entryId='" + entryId + '\'' +
                ", updateTime=" + updateTime +
                ", consignorCname='" + consignorCname + '\'' +
                ", billNo='" + billNo + '\'' +
                ", AddTime=" + AddTime +
                ", goodsNum='" + goodsNum + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", trafName='" + trafName + '\'' +
                ", oriData='" + oriData + '\'' +
                ", sendmsg_Delare=" + sendmsg_Delare +
                ", sendmsg_Check=" + sendmsg_Check +
                ", sendmsg_Clearance=" + sendmsg_Clearance +
                ", contrNo='" + contrNo + '\'' +
                ", customMasterName='" + customMasterName + '\'' +
                ", contactsName='" + contactsName + '\'' +
                ", contactsCompany='" + contactsCompany + '\'' +
                ", reMark='" + reMark + '\'' +
                ", isChecked=" + isChecked +
                ", isClearanced=" + isClearanced +
                ", cusDecStatus='" + cusDecStatus + '\'' +
                ", sendmsg_record=" + sendmsg_record +
                ", FromAgentCode='" + FromAgentCode + '\'' +
                ", company='" + company + '\'' +
                ", claimTime=" + claimTime +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                ", sortName='" + sortName + '\'' +
                ", sortFormat='" + sortFormat + '\'' +
                ", clicks=" + clicks +
                '}';
    }
}