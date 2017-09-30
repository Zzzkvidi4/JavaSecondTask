package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.validators.IDNotExistsValidator;
import com.zzzkvidi4.validators.IntegerGreaterZeroValidator;
import com.zzzkvidi4.validators.PatternValidator;
import com.zzzkvidi4.validators.StringNotEmptyValidator;

import java.sql.*;

public class AddUserCommand extends Command {
    private Connection connection;

    public AddUserCommand(String title, Connection connection) {
        super(title);
        this.connection = connection;
    }

    @Override
    public void execute() {
        try (
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO user (id_user, name, surname, login, email) VALUES (?, ?, ?, ?, ?);"
                )
        ) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user");
            int id = HelpUtils.getValueCLI(
                    "Введите id пользователя: ",
                    "abort",
                    new IDNotExistsValidator("Пользователь с таким id уже существует!", -1, rs)
            );

            String name = HelpUtils.getValueCLI(
                    "Введите имя пользователя: ",
                    "abort",
                    new StringNotEmptyValidator("Имя пользователя не должно быть пустым!")
            );

            String surname = HelpUtils.getValueCLI(
                    "Введите фамилию пользователя: ",
                    "abort",
                    new StringNotEmptyValidator("Фамилия пользователя не должна быть пустой!")
            );

            String email = HelpUtils.getValueCLI(
                    "Введите электронный адрес пользователя: ",
                    "abort",
                    new PatternValidator(
                            "Email введен некорректно!",
                            "",
                            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
            );

            String login = HelpUtils.getValueCLI(
                    "Введите логин пользователя: ",
                    "abort",
                    new StringNotEmptyValidator("Логи не должен быть пустым!")
            );
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setString(4, login);
            preparedStatement.setString(5, email);
            preparedStatement.executeUpdate();

        }
        catch (AbortOperationException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных!");
        }
    }
}
