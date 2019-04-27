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
    private String birthday;
    private String tel;
    private String lastCallTime;
    private String lastInspectTime;
    private String remarks;
    @Generated(hash = 925721177)
    public Contacts(Long id, @NotNull String userId, @NotNull String contactedId,
            String name, String birthday, String tel, String lastCallTime,
            String lastInspectTime, String remarks) {
        this.id = id;
        this.userId = userId;
        this.contactedId = contactedId;
        this.name = name;
        this.birthday = birthday;
        this.tel = tel;
        this.lastCallTime = lastCallTime;
        this.lastInspectTime = lastInspectTime;
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
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getTel() {
        return this.tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getLastCallTime() {
        return this.lastCallTime;
    }
    public void setLastCallTime(String lastCallTime) {
        this.lastCallTime = lastCallTime;
    }
    public String getLastInspectTime() {
        return this.lastInspectTime;
    }
    public void setLastInspectTime(String lastInspectTime) {
        this.lastInspectTime = lastInspectTime;
    }
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
