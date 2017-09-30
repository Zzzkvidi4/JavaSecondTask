package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.validators.PatternValidator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ImportToCSVCommand extends Command {
    private Connection connection;

    public ImportToCSVCommand(String title, Connection connection) {
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
        PrintWriter writer = null;
        try (Statement statement = connection.createStatement()) {
            String fileName = HelpUtils.getValueCLI(
                    "Введите имя csv файла: ",
                    "abort",
                    new PatternValidator("Имя файла задано некорректно!", "", "[a-zA-Z]+\\.csv")
            );
            ResultSet rs = statement.executeQuery("SELECT * FROM user;");
            writer = new PrintWriter(new FileOutputStream(fileName));
            while (rs.next()){
                writer.println(HelpUtils.rowToUser(rs).toCSVFormat());
            }
        }
        catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных!");
        }
        catch (AbortOperationException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("Не удалось открыть файл для записи!");
        }
        finally {
            if (writer != null) writer.close();
        }
    }
}
