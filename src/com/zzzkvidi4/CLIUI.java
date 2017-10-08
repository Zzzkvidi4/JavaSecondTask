package com.zzzkvidi4;

import com.zzzkvidi4.casters.IntegerCaster;
import com.zzzkvidi4.casters.StringCaster;
import com.zzzkvidi4.commands.*;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.exceptions.NotInitializedException;
import com.zzzkvidi4.validators.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Роман on 07.10.2017.
 */
class CLIUI {
    private Connection connection;

    CLIUI(Connection connection) {
        this.connection = connection;
    }

    private User inputUser() throws AbortOperationException, SQLException {
        User user = new User();
        user.setId(inputNotExistingId());
        user.setName(inputUserName());
        user.setSurname(inputUserSurname());
        user.setEmail(inputEmail());
        user.setLogin(inputUserLogin());
        return user;
    }

    private User getUserFromDB(int id) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user WHERE id_user=" + id + ";");
            if (rs.next()) {
                return HelpUtils.rowToUser(rs);
            }
        }
        return null;
    }

    private String inputUserName() throws AbortOperationException {
        return inputNotEmptyString(
                "Имя пользователя не должно быть пустым!",
                "Введите имя пользователя: ",
                "abort"
        );
    }

    private String inputUserSurname() throws AbortOperationException {
        return inputNotEmptyString(
                "Фамилия пользователя не должна быть пустой!",
                "Введите фамилию пользователя: ",
                "abort"
        );
    }

    private String inputUserLogin() throws AbortOperationException {
        return inputNotEmptyString(
                "Логин пользователя не должен быть пустым!",
                "Введите логин пользователя: ",
                "abort"
        );
    }

    private int inputExistingId() throws AbortOperationException, SQLException {
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            ArrayList<BasicValidator<Integer>> idExistsValidators = new ArrayList<>();
            idExistsValidators.add(new IDExistsValidator("Пользователь с таким id не существует!", rs));
            return HelpUtils.getValueCLI(
                    "Введите id пользователя: ",
                    "abort",
                    idExistsValidators,
                    -1,
                    new IntegerCaster()
            );
        }
    }

    private String inputNotEmptyString(String validationFailsMsg, String inputTitle, String abortString) throws AbortOperationException {
        ArrayList<BasicValidator<String>> stringNotEmptyValidators = new ArrayList<>();
        stringNotEmptyValidators.add(new StringNotEmptyValidator(validationFailsMsg));
        return HelpUtils.getValueCLI(
                inputTitle,
                abortString,
                stringNotEmptyValidators,
                "",
                new StringCaster()
        );
    }

    private ArrayList<Integer> inputExistingIds() throws AbortOperationException, SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            List<Integer> existingIds = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                existingIds.add(rs.getInt("id_user"));
            }
            ArrayList<BasicValidator<String>> idsStringValidator = new ArrayList<>();
            idsStringValidator.add(new StringNotEmptyValidator("Строка не должна быть пустой!"));
            String idsString = HelpUtils.getValueCLI(
                    "Введите строку идентификаторов, разделенных пробелами: ",
                    "abort",
                    idsStringValidator,
                    "",
                    new StringCaster()
            );
            String[] idsArray = idsString.replaceAll("[^0-9 ]", " ").trim().split("[ ]+");
            for (String idString: idsArray) {
                try {
                    Integer intId = Integer.parseInt(idString);
                    if (existingIds.contains(intId)) {
                        ids.add(intId);
                        existingIds.remove(intId);
                    }
                }
                catch (NumberFormatException e) {

                }
            }
            return ids;
        }
    }

    private int inputNotExistingId() throws AbortOperationException, SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            List<BasicValidator<Integer>> IDExistsValidators = new ArrayList<>();
            IDExistsValidators.add(new IDNotExistsValidator("Пользователь с таким id существует!", rs));
            IDExistsValidators.add(new IntegerGreaterZeroValidator("Id должен быть больше нуля!"));
            return HelpUtils.getValueCLI(
                    "Введите id пользователя: ",
                    "abort",
                    IDExistsValidators,
                    -1,
                    new IntegerCaster()
            );
        }
    }

    private int inputIdToFilter() throws AbortOperationException {
        return HelpUtils.getValueCLI(
                "Введите id для фильтрации (>=): ",
                "abort",
                new ArrayList<>(),
                -1,
                new IntegerCaster()
        );
    }

    private String inputNotEmptyStringMatchingPattern(String patternValidationFails, String emptyValidationFails, String pattern, String abortString, String inputTitle) throws AbortOperationException  {
        ArrayList<BasicValidator<String>> fileNameValidators = new ArrayList<>();
        fileNameValidators.add(new PatternValidator(patternValidationFails,  pattern));
        fileNameValidators.add(new StringNotEmptyValidator(emptyValidationFails));
        return HelpUtils.getValueCLI(
                inputTitle,
                abortString,
                fileNameValidators,
                "",
                new StringCaster()
        );
    }

    private String inputCSVFileName() throws AbortOperationException {
        return inputNotEmptyStringMatchingPattern(
                "Имя файла задано некорректно!",
                "Имя файла не должно быть пустым!",
                "[a-zA-Z0-9]+\\.csv",
                "abort",
                "Введите имя файла для экспорта: "
        );
    }

    private String inputEmail() throws AbortOperationException {
        return inputNotEmptyStringMatchingPattern(
                "Email введен некорректно!",
                "Email не должен быть пустым!",
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
                "abort",
                "Введите электронный адрес пользователя: "
        );
    }

    private CommandList initializeCommandList(){
        CommandList commands = new CommandList();
        commands.addCommand(new AddUserCommand("Добавить пользователя.", connection));
        commands.addCommand(new ExportToCSVCommand("Импортировать из csv формата.", connection));
        commands.addCommand(new DeleteUserCommand("Удалить пользователя.", connection));
        commands.addCommand(new PrintUsersCommand("Вывести пользователей.", connection));
        commands.addCommand(new EditUserCommand("Редактировать пользователя.", connection));
        commands.addCommand(new ImportFromCSVCommand("Экспорт в csv формат.", connection));
        commands.addCommand(new SortCommand("Отсортировать пользователей по логину.", connection));
        commands.addCommand(new FilterUserCommand("Отфильтровать пользователей по id.", connection));
        commands.addCommand(new DeleteUsersCommand("Удалить пользователей", connection));
        commands.addCommand(new ExitCommand("Выход."));
        return commands;
    }

    private void specifyCommandsArgs(Command cmd, List<Object> args) throws AbortOperationException, SQLException, IllegalArgumentException {
        if (cmd instanceof AddUserCommand) {
            args.add(inputUser());
        } else if (cmd instanceof DeleteUserCommand) {
            args.add(inputExistingId());
        } else if (cmd instanceof DeleteUsersCommand) {
            args.addAll(inputExistingIds());
        } else if ((cmd instanceof ExportToCSVCommand) || (cmd instanceof ImportFromCSVCommand)) {
            args.add(inputCSVFileName());
        } else if ((cmd instanceof PrintUsersCommand) || (cmd instanceof SortCommand) || (cmd instanceof FilterUserCommand)) {
            args.add(System.out);
            if (cmd instanceof FilterUserCommand) {
                args.add(inputIdToFilter());
            }
        }
    }

    private CommandList initializeEditionCommandList(User user) {
        CommandList editionCommands = new CommandList();
        editionCommands.addCommand(new EditUserIdCommand("Изменить id пользователя.", user, connection));
        editionCommands.addCommand(new EditUserNameCommand("Изменить имя пользователя.", user));
        editionCommands.addCommand(new EditUserSurnameCommand("Изменить фамилию пользователя.", user));
        editionCommands.addCommand(new EditUserLoginCommand("Изменить логин пользователя.", user));
        editionCommands.addCommand(new EditUserEmailCommand("Изменить email пользователя.", user));
        editionCommands.addCommand(new ExitCommand("Назад."));
        return editionCommands;
    }

    private void specifyEditionCommandsArgs(Command editionCmd, List<Object> editionArgs, User user) throws AbortOperationException, SQLException, IllegalArgumentException{
        if (editionCmd instanceof EditUserIdCommand) {
            System.out.println("Текущее значение id: " + user.getId());
            editionArgs.add(inputNotExistingId());
        } else if (editionCmd instanceof EditUserNameCommand) {
            System.out.println("Текущее имя: " + user.getName());
            editionArgs.add(inputUserName());
        } else if (editionCmd instanceof EditUserSurnameCommand) {
            System.out.println("Текущая фамилия: " + user.getSurname());
            editionArgs.add(inputUserSurname());
        } else if (editionCmd instanceof EditUserLoginCommand) {
            System.out.println("Текущий логин: " + user.getLogin());
            editionArgs.add(inputUserLogin());
        } else if (editionCmd instanceof EditUserEmailCommand) {
            System.out.println("Текущий email: " + user.getEmail());
            editionArgs.add(inputEmail());
        }
    }

    void start() {
        int cmdNumber = -1;
        CommandList commands = initializeCommandList();
        int actualSize = commands.actualSize();
        while (cmdNumber != actualSize) {
            commands.printCommandTitles("Меню:");
            ArrayList<BasicValidator<Integer>> inputValidator = new ArrayList<>();
            inputValidator.add(new IntegerBetweenBoundariesValidator("Число должно быть между 1 и " + actualSize + "!", 1, actualSize));
            cmdNumber = HelpUtils.getValueCLIWithoutAbort("--> ", inputValidator, -1, new IntegerCaster());
            Command cmd = commands.getCommand(cmdNumber - 1);
            List<Object> args = new ArrayList<>();
            try {
                if (!(cmd instanceof EditUserCommand)) {
                    specifyCommandsArgs(cmd, args);
                } else {
                    int id = inputExistingId();
                    User user = getUserFromDB(id);
                    if (user != null) {
                        CommandList editionCommands = initializeEditionCommandList(user);
                        int editionCommandsSize = editionCommands.actualSize();
                        int editionCmdNumber = -1;
                        while (editionCmdNumber != editionCommandsSize) {
                            System.out.println(user);
                            editionCommands.printCommandTitles("Меню редактирования: ");
                            List<BasicValidator<Integer>> cmdsValidator = new ArrayList<>();
                            cmdsValidator.add(new IntegerBetweenBoundariesValidator("Число должно быть между 1 и " + editionCommandsSize + "!", 1, editionCommandsSize));
                            editionCmdNumber = HelpUtils.getValueCLIWithoutAbort("--> ", cmdsValidator, -1, new IntegerCaster());
                            Command editionCmd = editionCommands.getCommand(editionCmdNumber - 1);
                            List<Object> editionArgs = new ArrayList<>();
                            try {
                                specifyEditionCommandsArgs(editionCmd, editionArgs, user);
                                editionCmd.initialize(editionArgs);
                                editionCmd.execute();
                            }
                            catch(NotInitializedException|IllegalArgumentException|AbortOperationException e) {
                                System.out.println(e.getMessage());
                            }
                            catch (SQLException e) {
                                System.out.println("Ошибка соединения с базой данных!");
                            }
                        }
                        args.add(id);
                        args.add(user);
                    }
                }
                cmd.initialize(args);
                cmd.execute();
            } catch(AbortOperationException | NotInitializedException e) {
                System.out.println(e.getMessage());
            }
            catch(SQLException e) {
                System.out.println("Ошибка соединения с базой данных!");
            }
            catch(FileNotFoundException e) {
                System.out.println("Невозможно открыть файл!");
            }
            catch(IOException e) {
                System.out.println("Ошибка работы с файлами!");
            }
        }
    }

}
