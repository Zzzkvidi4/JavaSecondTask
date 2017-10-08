package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.comparators.LoginComparator;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.predicates.IdGreaterPredicate;
import com.zzzkvidi4.validators.IntegerGreaterZeroValidator;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FilterUserCommand extends Command {
    private Connection connection;
    private int id;
    private PrintStream out;

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
    public void initialize(List<Object> args) {
        boolean isIdInitialized = false, isOutputStreamInitialized = false;
        for(Object obj: args){
            if (obj instanceof Integer) {
                id = (Integer) obj;
                isIdInitialized = true;
            } else if (obj instanceof OutputStream) {
                out = (PrintStream) obj;
                isOutputStreamInitialized = true;
            }
        }
        isInitialized = isIdInitialized && isOutputStreamInitialized;
    }

    @Override
    public void execute() throws NotInitializedException, SQLException {
        if (!isInitialized) {
            throw new NotInitializedException("Значение id для фильтрации или поток вывода не установлены!");
        } else {
            isInitialized = false;
        }
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user;");
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(HelpUtils.rowToUser(rs));
            }
            List<User> filteredUserList = new ArrayList<>();
            userList.stream().filter(new IdGreaterPredicate(id)).forEach(filteredUserList::add);
            out.println();
            for(User user: filteredUserList) {
                out.println(user);
            }
            out.println();
        }
    }
}
