package org.bankapplication.mappers;

import org.bankapplication.models.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<Account> {
    @Override
    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
        int acc_id = resultSet.getInt("acc_id");
        String gov_id = resultSet.getString("gov_id");
        String acc_code = resultSet.getString("acc_code");
        String creation_date = resultSet.getString("creation_date");
        long amount = resultSet.getLong("amount");
        boolean isBlocked = resultSet.getBoolean("isBlocked");
        return new Account(acc_id,gov_id,acc_code,creation_date,amount,isBlocked);
    }
}
