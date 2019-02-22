package org.fsin.matomat;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.dao.UsersDAO;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import java.util.Base64;

import java.security.MessageDigest;


public class Main {
    public static void main(String[] argv) {

        Database db;

        try{
            db = new Database("127.0.0.1", "matohmat", "matomat_system", "test");

            TestScenario test = new TestScenario(db);
            test.run();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

/*
Entitys:

Money
Product
User(Admin)
Transaction(Purchase, Transfer, Oredr)
 */