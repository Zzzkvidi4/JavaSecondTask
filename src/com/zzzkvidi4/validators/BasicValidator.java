package com.zzzkvidi4.validators;

/**
 * Created by Роман on 14.09.2017.
 */
public abstract class BasicValidator<T> {
    BasicValidator(String validationFailsMsg){
        this.validationFailsMsg = validationFailsMsg;
    }

    public abstract boolean validate(T value);

    private String validationFailsMsg;

    public String message(){
        return validationFailsMsg;
    }
}
