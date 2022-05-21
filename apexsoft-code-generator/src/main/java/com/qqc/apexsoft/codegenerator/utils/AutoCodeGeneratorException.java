package com.qqc.apexsoft.codegenerator.utils;

public class AutoCodeGeneratorException extends RuntimeException {
    public AutoCodeGeneratorException() {
    }

    public AutoCodeGeneratorException(String message) {
        super(message);
    }

    public AutoCodeGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutoCodeGeneratorException(Throwable cause) {
        super(cause);
    }

    public AutoCodeGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
