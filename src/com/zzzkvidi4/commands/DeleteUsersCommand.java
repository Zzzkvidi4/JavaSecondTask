package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.validators.StringNotEmptyValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteUsersCommand extends Command {
    private Connection connection;

    public DeleteUsersCommand(String title, Connection connection) {
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
        try (
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id_user=?;")
        ){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            List<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("id_user"));
            }
            String stringIds = HelpUtils.getValueCLI(
                    "Введите строку id, разделенных пробелами: ",
                    "abort",
                    new StringNotEmptyValidator("Строка не должна быть пустой!")
            );
            String[] idsArray = stringIds.replaceAll("[^0-9 ]", " ").trim().split("[ ]+");
            int count = 0;
            for (String id: idsArray){
                try {
                    Integer intId = Integer.parseInt(id);
                    if (ids.contains(intId)) {
                        ++count;
                        preparedStatement.setInt(1, intId);
                        ids.remove(intId);
                        preparedStatement.addBatch();
                    }
                }
                catch (NumberFormatException e) {

                }
            }
            preparedStatement.executeUpdate();
            System.out.println("Успешно удалено " + count + " элементов из " + idsArray.length + ".");
        }
        catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных!");
        }
        catch (AbortOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
