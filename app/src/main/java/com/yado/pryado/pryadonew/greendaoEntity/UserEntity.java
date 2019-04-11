package com.yado.pryado.pryadonew.greendaoEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserEntity {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String username;
    private String password;
    private String Sessionid;
    @Generated(hash = 1405648987)
    public UserEntity(Long id, @NotNull String username, String password,
            String Sessionid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.Sessionid = Sessionid;
    }
    @Generated(hash = 1433178141)
    public UserEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSessionid() {
        return this.Sessionid;
    }
    public void setSessionid(String Sessionid) {
        this.Sessionid = Sessionid;
    }
}