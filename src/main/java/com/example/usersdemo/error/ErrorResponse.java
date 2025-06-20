package com.example.usersdemo.error;

public class ErrorResponse {

    private final int code;
    private final String message;

    /**
     * 
     * @param code    {@link Integer}
     * @param message {@link String}
     */
    public ErrorResponse(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
