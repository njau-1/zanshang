package com.zanshang.framework;

/**
 * Created by xuming on 15/8/17.
 */
public class CodeExpireException extends RuntimeException{
    public CodeExpireException() {
    }

    public CodeExpireException(String message) {
        super(message);
    }

    public CodeExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeExpireException(Throwable cause) {
        super(cause);
    }

    public CodeExpireException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
