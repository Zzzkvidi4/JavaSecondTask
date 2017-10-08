package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.comparators.LoginComparator;
import com.zzzkvidi4.exceptions.NotInitializedException;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SortCommand extends Command {
    private Connection connection;
    private PrintStream out;

    public SortCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    @Override
    public boolean isEnabled() {
        try (Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS cnt FROM user;");
            return ((rs.next()) && (rs.getInt("cnt") > 1));
        }
        catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void initialize(List<Object> args) {
        for(Object obj: args) {
            if (obj instanceof PrintStream) {
                out = (PrintStream) obj;
                isInitialized = true;
            }
        }
    }

    @Override
    public void execute() throws NotInitializedException, SQLException {
        if (!isInitialized) {
            throw new NotInitializedException("Выходной поток не установлен!");
        } else {
            isInitialized = false;
        }
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user;");
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(HelpUtils.rowToUser(rs));
            }
            userList.sort(new LoginComparator());
            out.println();
            for (User user : userList) {
                out.println(user);
            }
            out.println();
        }
    }
}
