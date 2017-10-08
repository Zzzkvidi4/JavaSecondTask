package com.zzzkvidi4;

import com.zzzkvidi4.casters.IntegerCaster;
import com.zzzkvidi4.casters.StringCaster;
import com.zzzkvidi4.commands.*;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Роман on 07.10.2017.
 */
public class CLIUI {
    private Connection connection;

    public CLIUI(Connection connection) {
        this.connection = connection;
    }

    public User inputUser() throws AbortOperationException, SQLException {
        User user = new User();
        try(Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user");
            ArrayList<BasicValidator<Integer>> idValidators = new ArrayList<>();
            idValidators.add(new IDNotExistsValidator("Пользователь с таким id уже существует!", rs));
            ArrayList<BasicValidator<String>> stringNotEmptyValidators = new ArrayList<>();
            stringNotEmptyValidators.add(new StringNotEmptyValidator("Имя пользователя не должно быть пустым!"));
            ArrayList<BasicValidator<String>> emailValidators = new ArrayList<>();
            emailValidators.add(new StringNotEmptyValidator("Email не должен быть пустым!"));
            emailValidators.add(new PatternValidator("Email введен некорректно!", "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"));
            int id = HelpUtils.getValueCLI(
                    "Введите id пользователя: ",
                    "abort",
                    idValidators,
                    -1,
                    new IntegerCaster()
            );

            String name = HelpUtils.getValueCLI(
                    "Введите имя пользователя: ",
                    "abort",
                    stringNotEmptyValidators,
                    "",
                    new StringCaster()
            );

            String surname = HelpUtils.getValueCLI(
                    "Введите фамилию пользователя: ",
                    "abort",
                    stringNotEmptyValidators,
                    "",
                    new StringCaster()
            );

            String email = HelpUtils.getValueCLI(
                    "Введите электронный адрес пользователя: ",
                    "abort",
                    emailValidators,
                    "",
                    new StringCaster()
            );

            String login = HelpUtils.getValueCLI(
                    "Введите логин пользователя: ",
                    "abort",
                    stringNotEmptyValidators,
                    "",
                    new StringCaster()
            );
            user.setId(id);
            user.setName(name);
            user.setSurname(surname);
            user.setLogin(login);
            user.setEmail(email);
            return user;
        }
    }

    public int inputExistingId() throws AbortOperationException, SQLException {
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            ArrayList<BasicValidator<Integer>> idExistsValidators = new ArrayList<>();
            idExistsValidators.add(new IDExistsValidator("Пользователь с таким id не существует!", rs));
            int id = HelpUtils.getValueCLI(
                    "Введите id пользователя для удаления: ",
                    "abort",
                    idExistsValidators,
                    -1,
                    new IntegerCaster()
            );
            return id;
        }
    }

    public int inputNotExistingId() throws AbortOperationException, SQLException {
        return -1;
    }

    public int inputIdToFilter() throws AbortOperationException {
        return -1;
    }

    public String inputCSVFileName() throws AbortOperationException {
        return "";
    }

    public void start() {
        int cmdNumber = -1;
        CommandList commands = new CommandList();
        commands.addCommand(new AddUserCommand("Добавить пользователя.", connection));
        commands.addCommand(new ExportFromCSVCommand("Импортировать из csv формата.", connection));
        commands.addCommand(new DeleteUserCommand("Удалить пользователя.", connection));
        commands.addCommand(new PrintUsersCommand("Вывести пользователей.", connection));
        commands.addCommand(new EditUserCommand("Редактировать пользователя.", connection));
        commands.addCommand(new ImportToCSVCommand("Экспорт в csv формат.", connection));
        commands.addCommand(new SortCommand("Отсортировать пользователей по логину.", connection));
        commands.addCommand(new FilterUserCommand("Отфильтровать пользователей по id.", connection));
        commands.addCommand(new DeleteUsersCommand("Удалить пользователей", connection));
        commands.addCommand(new ExitCommand("Выход."));
        int actualSize = commands.actualSize();
        while (cmdNumber != actualSize) {
            commands.printCommandTitles("Меню:");
            ArrayList<BasicValidator<Integer>> inputValidator = new ArrayList<>();
            inputValidator.add(new IntegerBetweenBoundariesValidator("Число должно быть между 1 и " + actualSize + "!", 1, actualSize));
            cmdNumber = HelpUtils.getValueCLIWithoutAbort("--> ", inputValidator, -1, new IntegerCaster());
            Command cmd = commands.getCommand(cmdNumber - 1);
            try {
                if (cmd instanceof AddUserCommand) {
                    AddUserCommand add = (AddUserCommand) cmd;
                    User user = inputUser();
                    add.initialize(user);
                    add.execute();
                } else if (cmd instanceof DeleteUserCommand) {
                    DeleteUserCommand delete = (DeleteUserCommand) cmd;
                    int id = inputExistingId();
                    delete.initialize(id);
                    delete.execute();
                }
            } catch(AbortOperationException | NotInitializedException e) {
                System.out.println(e.getMessage());
            }
            catch(SQLException e) {
                System.out.println("Ошибка соединения с базой данных!");
            }
        }
    }

    public int getDBEntitiesCount(){
        try(Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS cnt FROM user;");
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        }
        catch (SQLException e) {

        }
        return -1;
    }

    public int getPositionOfCommand(int index){
        switch(getDBEntitiesCount()){
            case -1: {
                return -1;
            }
            case 0: {
                switch (index) {
                    case 1:{
                        return 0;
                        break;
                    }
                    case 2: {
                        return 8;
                    }
                }
            }
            case 1: {

            }
            default: {

            }
        }
    }

}
