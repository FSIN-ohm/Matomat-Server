package org.fsin.matomat;

import org.fsin.matomat.database.dao.UsersDAO;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Main {
    public static void main(String[] argv) {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://127.0.0.1/matohmat");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            JdbcTemplate template = new JdbcTemplate(dataSource);
            UserEntry user = new UserEntry();
            user.setAuthHash(new byte[]{1,2,3,4,5,4,3,2,1});
            new UsersDAO(template).addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
