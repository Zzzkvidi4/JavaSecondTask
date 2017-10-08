package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.StringNotEmptyValidator;

import java.util.List;

public class EditUserNameCommand extends Command {
    private User user;
    private String name;

    public EditUserNameCommand(String title, User user) {
        super(title);
        this.user = user;
    }


    @Override
    public void initialize(List<Object> args) {
        for(Object obj: args) {
            if (obj instanceof String) {
                name = (String) obj;
                isInitialized = true;
            }
        }
    }

    @Override
    public void execute() throws NotInitializedException, IllegalArgumentException {
        if (!isInitialized) {
            throw new NotInitializedException("Новое имя пользователя не установлено!");
        } else {
            isInitialized = false;
        }
        if (!(new StringNotEmptyValidator("Имя пользователя не может быть пустым!")).validate(name)) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым!");
        }
        user.setName(name);
    }
}
