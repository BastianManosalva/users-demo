package com.example.usersdemo.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 444215330868704757L;

    private String code;

    /**
     * 
     * @param code
     * @param message
     */
    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
