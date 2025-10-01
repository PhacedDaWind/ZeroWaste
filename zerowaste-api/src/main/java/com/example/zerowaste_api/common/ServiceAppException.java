package com.example.zerowaste_api.common;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class ServiceAppException extends AuthenticationException {

    private static final long serialVersionUID = 1L;
    private final int serviceAppStatus;
    private Object[] messageArgs;
    private boolean useErrorMessage = true;

    public ServiceAppException(HttpStatus httpStatus, String message) {
        super(message);
        this.serviceAppStatus = httpStatus.value();
        this.messageArgs = null;
    }

    public ServiceAppException(int serviceAppStatus, String message) {
        super(message);
        this.serviceAppStatus = serviceAppStatus;
        this.messageArgs = null;
    }

    public ServiceAppException(int serviceAppStatus, String message, Object... messageArgs) {
        super(message);
        this.serviceAppStatus = serviceAppStatus;
        this.messageArgs = messageArgs;
    }

    public ServiceAppException(HttpStatus httpStatus, String message, Object... messageArgs) {
        super(message);
        this.serviceAppStatus = httpStatus.value();
        this.messageArgs = messageArgs;
    }

    public ServiceAppException(HttpStatus httpStatus, String message, Boolean useErrorMessage, Object... messageArgs) {
        super(message);
        this.serviceAppStatus = httpStatus.value();
        this.messageArgs = messageArgs;
        this.useErrorMessage = useErrorMessage;
    }

    public ServiceAppException(int serviceAppStatus, String message, Throwable cause) {
        super(message, cause);
        this.serviceAppStatus = serviceAppStatus;
        this.messageArgs = null;
    }

    public ServiceAppException(int serviceAppStatus, String message, Throwable cause, Object... messageArgs) {
        super(message, cause);
        this.serviceAppStatus = serviceAppStatus;
        this.messageArgs = messageArgs;
    }

    public int getServiceAppStatusCode() {
        return serviceAppStatus;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public boolean getUseErrorMessage() {
        return this.useErrorMessage;
    }
}
