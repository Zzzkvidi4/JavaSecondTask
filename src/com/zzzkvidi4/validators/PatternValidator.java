package com.zzzkvidi4.validators;

import java.util.regex.Pattern;

public class PatternValidator extends BasicValidator<String> {
    private Pattern pattern;

    public PatternValidator(String validationFailsMsg, String initialValue, String pattern) {
        super(validationFailsMsg, initialValue);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean validate(String value) {
        return pattern.matcher(value).matches();
    }

    @Override
    public String cast(String value) {
        return value;
    }
}
