package com.zzzkvidi4.validators;

/**
 * Created by Роман on 15.09.2017.
 */
public class IntegerGreaterZeroValidator extends BasicValidator<Integer> {
    public IntegerGreaterZeroValidator(String validationFailsMsg){
        super(validationFailsMsg);
    }

    @Override
    public boolean validate(Integer value) {
        return value > 0;
    }
}
