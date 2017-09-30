package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.User;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.validators.*;

import java.sql.*;

public class EditUserCommand extends Command {

    private Connection connection;
    private User user;

    public EditUserCommand(String title, Connection connection) {
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
        try (
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET id_user=?, name=?, surname=?, login=?, email=? WHERE id_user=?;")
        ){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            int id = HelpUtils.getValueCLI(
                    "Введите id пользователя для редактирования: ",
                    "abort",
                    new IDExistsValidator("Пользователь с таким id не существует!", -1, rs)
            );
            rs = statement.executeQuery("SELECT * FROM user WHERE id_user=" + id + ";");
            user = rs.next() ? HelpUtils.rowToUser(rs) : null;
            if (user == null) {
                return;
            }
            preparedStatement.setInt(6, user.getId());
            CommandList commands = new CommandList();
            commands.addCommand(new EditUserIdCommand("Изменить ID."));
            commands.addCommand(new EditUserNameCommand("Изменить имя."));
            commands.addCommand(new EditUserSurnameCommand("Изменить фамилию."));
            commands.addCommand(new EditUserLoginCommand("Изменить логин."));
            commands.addCommand(new EditUserEmailCommand("Изменить email."));
            commands.addCommand(new ExitCommand("Назад."));
            int cmdIndex;
            int size = commands.actualSize();
            do {
                System.out.println(user);
                commands.printCommandTitles("Меню редактирования:");
                cmdIndex = HelpUtils.getValueCLIWithoutAbort(
                        "--> ",
                        new IntegerBetweenBoundariesValidator("Число должно быть между 1 и " + size + "!", 1, size)
                );
                commands.executeCommand(cmdIndex - 1);
            } while (cmdIndex != size);

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getLogin());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных!");
        }
        catch (AbortOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    private class EditUserIdCommand extends Command {

        public EditUserIdCommand(String title) {
            super(title);
        }

        @Override
        public void execute() {
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
                System.out.println("Текущее значение id: " + user.getId());
                int id = HelpUtils.getValueCLI(
                        "Введите новое значение id: ",
                        "abort",
                        new IDNotExistsValidator("Пользователь с таким id уже существует!", -1, rs)
                );
                user.setId(id);
            }
            catch (SQLException e) {
                System.out.println("Ошибка соединения с базой данных!");
            }
            catch (AbortOperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class EditUserNameCommand extends Command {

        public EditUserNameCommand(String title) {
            super(title);
        }

        @Override
        public void execute() {
            try {
                System.out.println("Текущее имя: " + user.getName());
                String name = HelpUtils.getValueCLI(
                        "Введите новое имя: ",
                        "abort",
                        new StringNotEmptyValidator("Имя пользователя не может быть пустым!")
                );
                user.setName(name);
            }
            catch (AbortOperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class EditUserSurnameCommand extends Command {

        public EditUserSurnameCommand(String title) {
            super(title);
        }

        @Override
        public void execute() {
            try {
                System.out.println("Текущая фамилия: " + user.getSurname());
                String surname = HelpUtils.getValueCLI(
                        "Введите новую фамилию: ",
                        "abort",
                        new StringNotEmptyValidator("Фамилия пользователя не может быть пустой!")
                );
                user.setSurname(surname);
            }
            catch (AbortOperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class EditUserLoginCommand extends Command {

        public EditUserLoginCommand(String title) {
            super(title);
        }

        @Override
        public void execute() {
            try {
                System.out.println("Текущий логин: " + user.getLogin());
                String login = HelpUtils.getValueCLI(
                        "Введите новый логин: ",
                        "abort",
                        new StringNotEmptyValidator("Логин не может быть пустым!")
                );
                user.setLogin(login);
            }
            catch (AbortOperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class EditUserEmailCommand extends Command {

        public EditUserEmailCommand(String title) {
            super(title);
        }

        @Override
        public void execute() {
            try {
                System.out.println("Текущее email: " + user.getEmail());
                String email = HelpUtils.getValueCLI(
                        "Введите новый email: ",
                        "abort",
                        new PatternValidator("Email введен некорректно!", "", "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
                );
                user.setEmail(email);
            }
            catch (AbortOperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
