package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditUserCommand extends Command {

    private Connection connection;
    private User user;
    private int id;

    public EditUserCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    @Override
    public boolean isEnabled() {
        try (Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS cnt FROM user;");
            return ((rs.next()) && (rs.getInt("cnt") > 0));
        }
        catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void initialize(List<Object> args) {
        boolean isUserInitialized = false, isIdInitialized = false;
        for (Object obj: args) {
            if (obj instanceof User) {
                user = (User) obj;
                isUserInitialized = true;
            } else if (obj instanceof Integer) {
                id = (Integer) obj;
                isIdInitialized = true;
            }
        }
        List<Integer> ids = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        }
        catch(SQLException e) {
            isInitialized = false;
        }
        isInitialized = isUserInitialized && isIdInitialized && ids.contains(id);
    }

    @Override
    public void execute() throws SQLException, NotInitializedException {
        if (!isInitialized) {
            throw new NotInitializedException("Пользователь для сохранения изменения и/или id пользователя не установлены!");
        } else {
            isInitialized = false;
        }
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET id_user=?, name=?, surname=?, login=?, email=? WHERE id_user=?;")
        ){
            preparedStatement.setInt(6, id);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getLogin());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.executeUpdate();
        }
    }

}
