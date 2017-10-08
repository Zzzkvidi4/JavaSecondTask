package com.zzzkvidi4.validators;

import java.util.regex.Pattern;

public class PatternValidator extends BasicValidator<String> {
    private Pattern pattern;

    public PatternValidator(String validationFailsMsg, String pattern) {
        super(validationFailsMsg);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean validate(String value) {
        return pattern.matcher(value).matches();
    }
}
