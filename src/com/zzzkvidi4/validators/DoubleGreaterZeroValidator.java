package com.zzzkvidi4.validators;

public class DoubleGreaterZeroValidator extends BasicValidator<Double> {
    public DoubleGreaterZeroValidator(String validationFailsMsg){
        super(validationFailsMsg, -1.0);
    }

    @Override
    public boolean validate(Double value) {
        return Math.abs(value) > Double.MIN_VALUE ;
    }

    @Override
    public Double cast(String value) {
        return Double.valueOf(value);
    }
}
