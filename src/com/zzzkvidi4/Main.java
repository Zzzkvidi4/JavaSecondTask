package com.zzzkvidi4;

import com.zzzkvidi4.commands.*;

import java.io.IOException;
import java.sql.*;

public class Main {
    /*
* "Написать программу (CLI, GUI) для работы с таблицей БД.

В таблице хранится список пользователей некоего сайта. Схема БД уже создана на момент запуска программы. Таблица содержит след. колонки:
- id [primary key]
- *имя
- *фамилия
- #логин/ник
- #почта

Программа должна позволять (все действия подразумевают выполнение SQL запросов с помощью JDBC):
- вывести список пользователей на экран +
- изменить конкретную запись +
- удалить конкретную запись +
- удалить несколько записей
- добавить нового пользователя в список +
- отфильтровать список пользователей на основе заданного критерия
- отсортировать список пользователей на основе заданного критерия
- импорт/экспорт данных из/в CSV, [JSON, XML] (имя файла задается пользователем)

Необходимо разделить приложение на модули:
- модель данных: класс, представляющий пользователя
- утилитный класс с методами добавления, удаления, редактирования, фильтрации, сортировки и т.д.
- утилитный класс для импорта/экспорта данных из/в CSV, [JSON, XML]
- класс пользовательского интерфейса (CLI, GUI)"
*/

    public static void main(String[] args) {
	// write your code here
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false", "zzzkvidi4", "CaTaClYsM");
            Statement statement = connection.createStatement()){
            Class.forName("com.mysql.jdbc.Driver");
            ResultSet rs = statement.executeQuery("SELECT * FROM user");
            while (rs.next()) {
                System.out.println(rs.getInt("id_user") + " " + rs.getString("name") + " " + rs.getString("surname") + " " + rs.getString("login") + " " + rs.getString("email"));
            }

            CommandList commands = new CommandList();
            commands.addCommand(new AddUserCommand("Добавить пользователя.", connection));
            commands.addCommand(new DeleteUserCommand("Удалить пользователя.", connection));
            commands.addCommand(new PrintUsersCommand("Вывести пользователей.", connection));
            commands.addCommand(new EditUserCommand("Редактировать пользователя.", connection));
            commands.addCommand(new SortCommand("Отсортировать пользователей.", connection));
            commands.addCommand(new ExitCommand("Выход."));
            HelpUtils.runCommandList("Меню:", commands);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
