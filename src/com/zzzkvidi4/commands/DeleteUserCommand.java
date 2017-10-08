package com.zzzkvidi4.commands;

import com.zzzkvidi4.exceptions.NotInitializedException;

import java.sql.*;
import java.util.List;

public class DeleteUserCommand extends Command {
    private Connection connection;
    private int id;

    public DeleteUserCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    @Override
    public void initialize(List<Object> args) {
        for (Object obj: args) {
            if (obj instanceof Integer) {
                this.id = (Integer) obj;
                isInitialized = true;
            }
        }
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
    public void execute() throws NotInitializedException, SQLException {
        if (!isInitialized) {
            throw new NotInitializedException("Id не инициализирован!");
        } else {
            isInitialized = false;
        }
        try (
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id_user=?;")
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
