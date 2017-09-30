package com.zzzkvidi4.validators;

/**
 * Created by Роман on 14.09.2017.
 */
public abstract class BasicValidator<T> {
    BasicValidator(String validationFailsMsg, T initialValue){
        this.validationFailsMsg = validationFailsMsg;
        this.initialValue = initialValue;
    }

    public abstract boolean validate(T value);

    private String validationFailsMsg;

    public String message(){
        return validationFailsMsg;
    }

    private T initialValue;

    public T initialValue(){
        return initialValue;
    }

    public abstract T cast(String value);
}
