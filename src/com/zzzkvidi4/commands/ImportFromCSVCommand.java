package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.PatternValidator;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ImportFromCSVCommand extends Command {
    private Connection connection;
    private String fileName = "";

    public ImportFromCSVCommand(String title, Connection connection) {
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
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+\\.csv");
        for(Object obj: args) {
            if ((obj instanceof String) && (pattern.matcher((String) obj).matches())) {
                fileName = (String)obj;
            }
        }
        isInitialized = fileName.equals("");
    }

    @Override
    public void execute() throws NotInitializedException, SQLException, IOException {
        BufferedReader reader = null;
        if (!isInitialized) {
            throw new NotInitializedException("Имя файла не установлено!");
        } else {
            isInitialized = false;
        }
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (id_user, name, surname, login, email) VALUES (?, ?, ?,?, ?);");
                Statement statement = connection.createStatement()
        ){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            List<Integer> ids = new ArrayList<>();
            while (rs.next()){
                ids.add(rs.getInt("id_user"));
            }
            reader = new BufferedReader(new FileReader(fileName));
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                try {
                    User user = User.fromCSVFormat(tmp);
                    if (!ids.contains(user.getId())) {
                        preparedStatement.setInt(1, user.getId());
                    } else {
                        int id = HelpUtils.getNotExistingId(ids);
                        preparedStatement.setInt(1, id);
                        ids.add(id);
                    }
                    preparedStatement.setString(2, user.getName());
                    preparedStatement.setString(3, user.getSurname());
                    preparedStatement.setString(4, user.getLogin());
                    preparedStatement.setString(5, user.getEmail());
                    preparedStatement.addBatch();
                }
                catch (IllegalArgumentException e) {
                }
            }
            preparedStatement.executeBatch();
        }
        finally {
            if (reader != null) reader.close();
        }
    }
}
