package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.casters.IntegerCaster;
import com.zzzkvidi4.casters.StringCaster;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddUserCommand extends Command {
    private Connection connection;
    private User user;

    public AddUserCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    public void initialize(User user){
        this.user = user;
        isInitialized = true;
    }

    @Override
    public void execute() throws NotInitializedException, SQLException {
        if (!isInitialized) {
            throw new NotInitializedException("Пользователь не установлен!");
        }
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO user (id_user, name, surname, login, email) VALUES (?, ?, ?, ?, ?);"
                )
        ) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getLogin());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.executeUpdate();
        }
    }
}
