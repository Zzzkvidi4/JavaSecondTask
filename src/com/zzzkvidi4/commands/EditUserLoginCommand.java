package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.StringNotEmptyValidator;

import java.util.List;

public class EditUserLoginCommand extends Command {

    private User user;
    private String login;

    public EditUserLoginCommand(String title, User user) {
        super(title);
        this.user = user;
    }

    @Override
    public void initialize(List<Object> args) {
        for(Object obj: args) {
            if (obj instanceof String) {
                login = (String) obj;
                isInitialized = true;
            }
        }
    }

    @Override
    public void execute() throws NotInitializedException, IllegalArgumentException {
        if (!isInitialized) {
            throw new NotInitializedException("Новый логин не установлен!");
        } else {
            isInitialized = false;
        }
        if (!(new StringNotEmptyValidator("Логин не должен быть пустым!")).validate(login)){
            throw new IllegalArgumentException("Логин не должен быть пустым!");
        }
        user.setLogin(login);
    }
}
