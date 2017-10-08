package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.IDNotExistsValidator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EditUserIdCommand extends Command {
    private Connection connection;
    private User user;
    private int id = -1;

    public EditUserIdCommand(String title, User user, Connection connection) {
        super(title);
        this.user = user;
        this.connection = connection;
    }

    @Override
    public void initialize(List<Object> args) {
        for (Object obj: args) {
            if (obj instanceof Integer) {
                id = (Integer) obj;
                isInitialized = id > 0;
            }
        }
    }

    @Override
    public void execute() throws SQLException, NotInitializedException, IllegalArgumentException {
        if (!isInitialized) {
            throw new NotInitializedException("Новое значение id не установлено!");
        } else {
            isInitialized = false;
        }
        try (Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            if (!(new IDNotExistsValidator("Пользователь с таким id уже существует!", rs).validate(id))){
                isInitialized = false;
                throw new IllegalArgumentException("Пользователь с таким id уже существует!");
            }
            user.setId(id);
        }
    }
}
