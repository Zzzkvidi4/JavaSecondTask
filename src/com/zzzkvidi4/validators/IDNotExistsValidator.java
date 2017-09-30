package com.zzzkvidi4.validators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Роман on 25.09.2017.
 */
public class IDNotExistsValidator extends BasicValidator<Integer> {
    private List<Integer> ids;

    public IDNotExistsValidator(String validationFailsMsg, Integer initialValue, ResultSet rs) throws SQLException {
        super(validationFailsMsg, initialValue);
        ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getInt(1));
        }
    }

    @Override
    public boolean validate(Integer value) {
        return !(ids.contains(value));
    }

    @Override
    public Integer cast(String value) {
        return Integer.valueOf(value);
    }
}
