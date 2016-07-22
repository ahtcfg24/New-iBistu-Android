package org.iflab.newbistu.models;

import java.io.Serializable;

/**
 * DreamFactory返回的错误信息模型
 */
public class ErrorMessage implements Serializable {

    private Error error = new Error();

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.error = new Error(message);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public ErrorException toException() {
        return new ErrorException(this);
    }

    /**
     * 自定义错误
     */
    public static class Error implements Serializable {

        private String message;

        private Long code = 0L;

        private String[] trace;

        private Context context;

        public Error() {
        }

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public String[] getTrace() {
            return trace;
        }

        public void setTrace(String[] trace) {
            this.trace = trace;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public static class Context implements Serializable {


            public Context() {
            }

            private String[] email = new String[0];

            private String[] password = new String[0];

            public String[] getEmail() {
                return email;
            }

            public void setEmail(String[] email) {
                this.email = email;
            }

            public String[] getPassword() {
                return password;
            }

            public void setPassword(String[] password) {
                this.password = password;
            }
        }
    }

    /**
     * 自定义错误异常
     */
    public static class ErrorException extends Exception {
        private ErrorMessage errorMessage;

        public ErrorException() {
        }

        public ErrorException(ErrorMessage errorMessage) {
            super(errorMessage.getError().getMessage());

            this.errorMessage = errorMessage != null ? errorMessage : new ErrorMessage("未知错误");
        }

        public ErrorMessage getErrorMessage() {
            return errorMessage;
        }
    }
}
