package org.iflab.newbistu.models;

import java.io.Serializable;

/**
 * 登录请求模型
 */
public class LoginRequest implements Serializable {

    private String email;

    private String password;

    private boolean remember_me;

    public LoginRequest(){}

    public LoginRequest(String email, String password, boolean remember_me) {
        this.email = email;
        this.password = password;
        this.remember_me = remember_me;
    }

    public boolean isRemember_me() {
        return remember_me;
    }

    public void setRemember_me(boolean remember_me) {
        this.remember_me = remember_me;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
