package org.fsin.matomat;


import org.fsin.matomat.database.dao.ProductStockDAO;
import org.fsin.matomat.database.model.ProductStockEntry;
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
            for(ProductStockEntry e :
                    new ProductStockDAO(template).getAll()) {
                System.out.println(e.getProduct_id() + " " + e.getUser_id() + " " + e.getSock());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
