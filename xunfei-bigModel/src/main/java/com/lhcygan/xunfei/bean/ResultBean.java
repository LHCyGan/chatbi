package com.lhcygan.xunfei.bean;

import lombok.*;

/**
 * @Author: LHCyGan
 * @CreateTime: 2023-05-22  11:04
 * @Description: TODO
 * @Version: 1.0
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResultBean<T> {

    private String errorCode;

    private String message;

    private T data;

    public ResultBean(T data) {
        this.errorCode = ErrorMessage.SUCCESS.getErrorCode();
        this.message = ErrorMessage.SUCCESS.getMessage();
        this.data = data;
    }

    public ResultBean(ErrorMessage errorMessage, T data) {
        this.errorCode = errorMessage.getErrorCode();
        this.message = errorMessage.getMessage();
        this.data = data;
    }


    public static <T> ResultBean success(T data) {
        ResultBean resultBean = new ResultBean(data);
        return resultBean;
    }

    public static <T> ResultBean fail(T data) {
        ResultBean resultBean = new ResultBean(ErrorMessage.FAIL.getErrorCode(), ErrorMessage.FAIL.getMessage(), data);
        return resultBean;
    }

    public enum ErrorMessage {

        SUCCESS("0", "success"),
        FAIL("001", "fail"),
        NOAUTH("1001", "非法访问");

        private String errorCode;
        private String message;

        ErrorMessage(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
