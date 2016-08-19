package org.iflab.ibistubydreamfactory.models;

/**
 * 服务器请求发送成功后返回的json对象模型
 */
public class SuccessModel extends BaseRecord{

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
