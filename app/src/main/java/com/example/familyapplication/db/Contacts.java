package com.example.familyapplication.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Contacts {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String userId;
    @NotNull
    private String contactedId;
    private String name;
    private String tel;

    private String birthday;
    private String remarkForBirth;

    private String lastCallTime;
    private String remarkForCall;

    private String lastInspectTime;
    private String remarkForInspect;

    private String remarks;

    @Generated(hash = 311574667)
    public Contacts(Long id, @NotNull String userId, @NotNull String contactedId,
            String name, String tel, String birthday, String remarkForBirth,
            String lastCallTime, String remarkForCall, String lastInspectTime,
            String remarkForInspect, String remarks) {
        this.id = id;
        this.userId = userId;
        this.contactedId = contactedId;
        this.name = name;
        this.tel = tel;
        this.birthday = birthday;
        this.remarkForBirth = remarkForBirth;
        this.lastCallTime = lastCallTime;
        this.remarkForCall = remarkForCall;
        this.lastInspectTime = lastInspectTime;
        this.remarkForInspect = remarkForInspect;
        this.remarks = remarks;
    }

    @Generated(hash = 1804918957)
    public Contacts() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactedId() {
        return this.contactedId;
    }

    public void setContactedId(String contactedId) {
        this.contactedId = contactedId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRemarkForBirth() {
        return this.remarkForBirth;
    }

    public void setRemarkForBirth(String remarkForBirth) {
        this.remarkForBirth = remarkForBirth;
    }

    public String getLastCallTime() {
        return this.lastCallTime;
    }

    public void setLastCallTime(String lastCallTime) {
        this.lastCallTime = lastCallTime;
    }

    public String getRemarkForCall() {
        return this.remarkForCall;
    }

    public void setRemarkForCall(String remarkForCall) {
        this.remarkForCall = remarkForCall;
    }

    public String getLastInspectTime() {
        return this.lastInspectTime;
    }

    public void setLastInspectTime(String lastInspectTime) {
        this.lastInspectTime = lastInspectTime;
    }

    public String getRemarkForInspect() {
        return this.remarkForInspect;
    }

    public void setRemarkForInspect(String remarkForInspect) {
        this.remarkForInspect = remarkForInspect;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
