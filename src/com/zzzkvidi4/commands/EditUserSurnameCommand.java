package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.StringNotEmptyValidator;

import java.util.List;

public class EditUserSurnameCommand extends Command {
    private User user;
    private String surname;

    public EditUserSurnameCommand(String title, User user) {
        super(title);
    }

    @Override
    public void initialize(List<Object> args) {
        for(Object obj: args) {
            if (obj instanceof String) {
                surname = (String) obj;
                isInitialized = true;
            }
        }
    }

    @Override
    public void execute() throws NotInitializedException, IllegalArgumentException {
        if (!isInitialized) {
            throw new NotInitializedException("Новая фамилия не установлена!");
        } else {
            isInitialized = false;
        }
        if (!(new StringNotEmptyValidator("Фамилия пользователя не может быть пустой!").validate(surname))) {
            throw new IllegalArgumentException("Фамилия пользователя не может быть пустой!");
        }
        user.setSurname(surname);
    }
}
