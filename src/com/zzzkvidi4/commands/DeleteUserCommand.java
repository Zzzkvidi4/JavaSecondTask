package com.zzzkvidi4.commands;

import com.zzzkvidi4.HelpUtils;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.validators.IDExistsValidator;

import java.sql.*;

public class DeleteUserCommand extends Command {
    private Connection connection;

    public DeleteUserCommand(String title, Connection connection) {
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
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id_user=?;")
        ){
            ResultSet rs = statement.executeQuery("SELECT DISTINCT id_user FROM user;");
            int id = HelpUtils.getValueCLI(
                    "Введите id пользователя для удаления: ",
                    "abort",
                    new IDExistsValidator("Пользователь с таким id не существует!", -1, rs)
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        catch (AbortOperationException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных!");
        }
    }
}
