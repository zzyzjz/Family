package com.example.familyapplication.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Users {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String userId;
    @NotNull
    private String password;
    private String nickname;
    private int head;
    @Generated(hash = 71811089)
    public Users(Long id, @NotNull String userId, @NotNull String password,
            String nickname, int head) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.head = head;
    }
    @Generated(hash = 2146996206)
    public Users() {
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
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public int getHead() {
        return this.head;
    }
    public void setHead(int head) {
        this.head = head;
    }


}
