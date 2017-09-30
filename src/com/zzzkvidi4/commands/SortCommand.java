package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.comparators.LoginComparator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SortCommand extends Command {
    private Connection connection;

    public SortCommand(String title, Connection connection) {
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
    public void execute() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user;");
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(HelpUtils.rowToUser(rs));
            }
            userList.sort(new LoginComparator());
            System.out.println();
            for(User user: userList) {
                System.out.println(user);
            }
            System.out.println();
        }
        catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных!");
        }
    }
}
