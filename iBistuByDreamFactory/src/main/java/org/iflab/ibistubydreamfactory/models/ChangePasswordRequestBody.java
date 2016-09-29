package org.iflab.ibistubydreamfactory.models;

/**
 * 修改密码请求体
 */
public class ChangePasswordRequestBody extends BaseRecord{
    private String old_password;
    private String new_password;
    private String email;

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
