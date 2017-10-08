package com.zzzkvidi4.commands;

import com.zzzkvidi4.exceptions.NotInitializedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteUsersCommand extends Command {
    private Connection connection;
    private ArrayList<Integer> ids;

    public DeleteUsersCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
        ids = new ArrayList<>();
    }

    @Override
    public boolean isEnabled() {
        try (Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS cnt FROM user;");
            int size = 0;
            if (rs.next()) {
                size = rs.getInt("cnt");
            }
            return size > 1;
        }
        catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void initialize(List<Object> args){
        for(Object obj: args){
            if (obj instanceof Integer) {
                ids.add((Integer) obj);
            }
        }
        isInitialized = ids.size() != 0;
    }

    @Override
    public void execute() throws NotInitializedException, SQLException {
        if (!isInitialized) {
            throw new NotInitializedException("Ни одного id для удаления не было добавлено!");
        } else {
            isInitialized = false;
        }
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id_user=?;");
                Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            List<Integer> existingIds = new ArrayList<>();
            while (rs.next()) {
                existingIds.add(rs.getInt("id_user"));
            }
            for (Integer id : ids) {
                if (existingIds.contains(id)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.addBatch();
                    existingIds.remove(id);
                }
            }
            preparedStatement.executeBatch();
        }
    }
}
