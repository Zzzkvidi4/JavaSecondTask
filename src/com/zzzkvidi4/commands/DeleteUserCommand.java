package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.IDExistsValidator;

import java.sql.*;

public class DeleteUserCommand extends Command {
    private Connection connection;
    private int id;

    public DeleteUserCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    public void initialize(int id) {
        this.id = id;
        isInitialized = true;
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
        }
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id_user=?;");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

    }
}
