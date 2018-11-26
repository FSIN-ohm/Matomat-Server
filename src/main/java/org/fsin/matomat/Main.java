package org.fsin.matomat;


import org.fsin.matomat.database.dao.UsersDAO;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Main {
    public static void main(String[] argv) {
        System.out.println("hallo welt");
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://127.0.0.1/matohmat");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            UsersDAO usersDAO = new UsersDAO(new JdbcTemplate(dataSource));
            for(UserEntry user : usersDAO.getAll()) {
                System.out.println((user.getId()) + " " + user.getAuthHash());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
