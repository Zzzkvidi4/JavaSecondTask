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

public class ExportToCSVCommand extends Command {
    private Connection connection;
    private String fileName = "";

    public ExportToCSVCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
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
        if (!isInitialized){
            throw new NotInitializedException("Имя файла не установлено!");
        } else {
            isInitialized = false;
        }
        PrintWriter writer = null;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user;");
            writer = new PrintWriter(new FileOutputStream(fileName));
            while (rs.next()){
                writer.println(HelpUtils.rowToUser(rs).toCSVFormat());
            }
        }
        finally {
            if (writer != null) writer.close();
        }
    }
}
