package com.zzzkvidi4.exceptions;

public class AbortOperationException extends Throwable {
    private String msg;

    public AbortOperationException(String msg){
        super(msg);
    }

    public AbortOperationException() {
        super();
    }
}
