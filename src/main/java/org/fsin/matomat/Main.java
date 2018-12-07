package org.fsin.matomat;

import org.fsin.matomat.database.dao.PurchaseDAO;
import org.fsin.matomat.database.model.PurchaseEntry;
import org.fsin.matomat.database.model.PurchasedProductEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.ArrayList;

public class Main {
    public static void main(String[] argv) {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://127.0.0.1/matohmat");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            JdbcTemplate template = new JdbcTemplate(dataSource);
            PurchaseEntry p = new PurchaseEntry();
            p.setSender_id(4);
            p.setRecipient_id(1);
            PurchasedProductEntry pp = new PurchasedProductEntry();
            pp.setProduct_id(1);
            pp.setCount(3);
            ArrayList<PurchasedProductEntry> ppList = new ArrayList<>();
            ppList.add(pp);
            pp = new PurchasedProductEntry();
            pp.setProduct_id(2);
            pp.setCount(5);
            ppList.add(pp);
            new PurchaseDAO(template).addNewPurchase(p, ppList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
