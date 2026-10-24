package com.tecsun.sisp.net.exception;

/**
 * 业务异常类
 *
 */
public class ServiceException extends RuntimeException{

	private String code;
    private String message;

    public ServiceException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
