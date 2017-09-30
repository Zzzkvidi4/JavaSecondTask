package com.zzzkvidi4.validators;

/**
 * Created by Роман on 15.09.2017.
 */
public class IntegerGreaterZeroValidator extends BasicValidator<Integer> {
    public IntegerGreaterZeroValidator(String validationFailsMsg, Integer initialValue){
        super(validationFailsMsg, initialValue);
    }

    @Override
    public boolean validate(Integer value) {
        return value > 0;
    }

    @Override
    public Integer cast(String value) {
        return Integer.valueOf(value);
    }
}
