package org.iflab.ibistubydreamfactory.models;

/**
 * 刷新token请求体
 */
public class RefreshTokenRequestBody extends BaseRecord{
    private String session_token;
    private String api_key;


    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }


}
