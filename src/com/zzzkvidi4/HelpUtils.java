package com.zzzkvidi4;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelpUtils {
    public  User rowToUser(ResultSet rs){
        try {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id_user"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setLogin(rs.getString("login"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
