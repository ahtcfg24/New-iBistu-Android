package org.iflab.ibistubydreamfactory.models;

import java.io.Serializable;

/**
 * 登录请求体模型
 */
public class LoginRequestBody implements Serializable {

    private String email;

    private String password;


    public LoginRequestBody(){}

    public LoginRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
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
