package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.PatternValidator;
import com.zzzkvidi4.validators.StringNotEmptyValidator;

import java.util.List;

public class EditUserEmailCommand extends Command {
    private User user;
    private String email;

    public EditUserEmailCommand(String title, User user) {
        super(title);
        this.user = user;
    }

    @Override
    public void initialize(List<Object> args) {
        for(Object obj: args) {
            if (obj instanceof String) {
                email = (String) obj;
                isInitialized = true;
            }
        }
    }

    @Override
    public void execute() throws NotInitializedException, IllegalArgumentException {
        if (!isInitialized) {
            throw new NotInitializedException("Новый email не установлен!");
        } else {
            isInitialized = false;
        }
        if (!(new StringNotEmptyValidator("Email не должен быть пустым!")).validate(email)){
            throw new IllegalArgumentException("Email не должен быть пустым!");
        }
        if (!(new PatternValidator("", "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")).validate(email)){
            throw new IllegalArgumentException("Email некорректен!");
        }
        user.setEmail(email);
    }
}
