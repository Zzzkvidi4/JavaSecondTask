package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.NotInitializedException;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PrintUsersCommand extends Command {
    private Connection connection;
    private PrintStream out = null;

    public PrintUsersCommand(String title, Connection connection) {
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
            throw new NotInitializedException("Поток вывода не установлен!");
        } else {
            isInitialized = false;
        }
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user");
            out.println();
            while (rs.next()) {
                User user = HelpUtils.rowToUser(rs);
                System.out.println(user);
            }
            System.out.println();
        }
    }
}
