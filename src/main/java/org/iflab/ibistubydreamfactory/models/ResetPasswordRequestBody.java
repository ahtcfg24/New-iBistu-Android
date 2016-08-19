package org.iflab.ibistubydreamfactory.models;

/**
 *
 */
public class ResetPasswordRequestBody extends BaseRecord{

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
