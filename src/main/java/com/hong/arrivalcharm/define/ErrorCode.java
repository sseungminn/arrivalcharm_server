package com.hong.arrivalcharm.define;

public enum ErrorCode {
	EXPIRED_TOKEN(4031),
	INVALID_TOKEN(4032);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
}
