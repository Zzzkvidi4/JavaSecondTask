package com.zzzkvidi4.validators;

public class IntegerBetweenBoundariesValidator extends BasicValidator<Integer> {
    private int leftBound, rightBound;
    public IntegerBetweenBoundariesValidator(String validationFailsMsg, int leftBound, int rightBound){
        super(validationFailsMsg);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    @Override
    public boolean validate(Integer value) {
        return (value >= leftBound) && (value <= rightBound);
    }
}
