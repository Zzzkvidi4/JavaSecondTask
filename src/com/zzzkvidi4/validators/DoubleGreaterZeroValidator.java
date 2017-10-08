package com.zzzkvidi4.validators;

public class DoubleGreaterZeroValidator extends BasicValidator<Double> {
    public DoubleGreaterZeroValidator(String validationFailsMsg){
        super(validationFailsMsg);
    }

    @Override
    public boolean validate(Double value) {
        return Math.abs(value) > Double.MIN_VALUE ;
    }
}
