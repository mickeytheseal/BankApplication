package org.bankapplication.mappers;

import org.bankapplication.models.Client;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet resultSet, int i) throws SQLException {
        String gov_id = resultSet.getString("gov_id");
        String full_name = resultSet.getString("full_name");
        String birth_date = resultSet.getString("birth_date");
        String phone_num = resultSet.getString("phone_num");
        String email = resultSet.getString("email");
        return new Client(gov_id,full_name,birth_date,phone_num,email);
    }
}
