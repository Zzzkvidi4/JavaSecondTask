package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.comparators.LoginComparator;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.predicates.IdGreaterPredicate;
import com.zzzkvidi4.validators.IntegerGreaterZeroValidator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FilterUserCommand extends Command {
    private Connection connection;

    public FilterUserCommand(String title, Connection connection) {
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
    public void execute() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user;");
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(HelpUtils.rowToUser(rs));
            }

            int id = HelpUtils.getValueCLI(
                    "Введите id для фильтрации (>=): ",
                    "abort",
                    new IntegerGreaterZeroValidator("ID должен быть больше нуля!", -1)
            );
            List<User> filteredUserList = new ArrayList<>();
            userList.stream().filter(new IdGreaterPredicate(id)).forEach(filteredUserList::add);
            System.out.println();
            for(User user: filteredUserList) {
                System.out.println(user);
            }
            System.out.println();
        }
        catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных!");
        }
        catch (AbortOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
