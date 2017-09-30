package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.validators.PatternValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportFromCSVCommand extends Command {
    private Connection connection;

    public ExportFromCSVCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    @Override
    public void execute() {
        BufferedReader reader = null;
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (id_user, name, surname, login, email) VALUES (?, ?, ?,?, ?);");
                Statement statement = connection.createStatement()
        ){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            List<Integer> ids = new ArrayList<>();
            while (rs.next()){
                ids.add(rs.getInt("id_user"));
            }
            String fileName = HelpUtils.getValueCLI(
                    "Введите имя файла для экспорта: ",
                    "abort",
                    new PatternValidator("Имя файла задано некорректно!", "", "[a-zA-Z0-9]+\\.csv")
            );
            reader = new BufferedReader(new FileReader(fileName));
            String tmp;
            int notRecognized = 0;
            int recognized = 0;
            int added = 0;
            while ((tmp = reader.readLine()) != null) {
                try {
                    User user = User.fromCSVFormat(tmp);
                    if (!ids.contains(user.getId())) {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, user.getName());
                        preparedStatement.setString(3, user.getSurname());
                        preparedStatement.setString(4, user.getLogin());
                        preparedStatement.setString(5, user.getEmail());
                        preparedStatement.addBatch();
                        ++added;
                    }
                    ++recognized;
                }
                catch (IllegalArgumentException e) {
                    ++notRecognized;
                }
            }
            preparedStatement.executeBatch();
            System.out.println("Распознано строк - " + recognized + ", не распознано - " + notRecognized + ", добавлено - " + added + ".");
        }
        catch (SQLException e) {
            System.out.println("Ошибка соединения с базой!");
        }
        catch (IOException e){
            System.out.println("Не удалось открыть файл для чтения!");
        }
        catch (AbortOperationException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if (reader != null) reader.close();
            }
            catch (IOException e) {
                System.out.println("Не удалось закрыть ресурс. Вообще фигня какая-то происходит...");
            }
        }
    }
}
