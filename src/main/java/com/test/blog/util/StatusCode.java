package com.test.blog.util;

public enum StatusCode {
    SUCCESS(200), INTERNALFAIL(500), NORESOURCE(400);

    private int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
