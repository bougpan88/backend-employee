package com.boug.employee.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomError {

    private int code;
    private String message;
    private String cause;

    public CustomError(int code, String message, String cause) {
        this.code = code;
        this.message = message;
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "CustomError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
