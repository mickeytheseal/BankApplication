package org.bankapplication.mappers;

import org.bankapplication.models.Client;
import org.bankapplication.models.Operation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OperationMapper implements RowMapper<Operation> {
    @Override
    public Operation mapRow(ResultSet resultSet, int i) throws SQLException {
        int entry_id = resultSet.getInt("entry_id");
        int acc_id = resultSet.getInt("acc_id");
        String operation = resultSet.getString("operation");
        String entry_date = resultSet.getString("entry_date");
        return new Operation(entry_id,acc_id,operation,entry_date);
    }
}
