package org.bankapplication;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBWorker {
    private static JdbcTemplate jdbcTemplate;
    private static NamedParameterJdbcTemplate npJdbcTemplate;
    private static String url = "jdbc:sqlserver://DESKTOP-0ETQHPB\\SQLEXPRESS;databaseName=BankDB";
    private static String driver_class = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static void setDataSource(String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver_class);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        jdbcTemplate = new JdbcTemplate(dataSource);
        npJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public static JdbcTemplate getJdbcTemplate() { return jdbcTemplate; }

    public static NamedParameterJdbcTemplate getNpJdbcTemplate() { return npJdbcTemplate; }
}
