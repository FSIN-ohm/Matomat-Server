package org.fsin.matomat;

import org.fsin.matomat.database.Database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication

public class Main {
    public static void main(String[] argv) {


        try{
            Database.init("127.0.0.1", "matohmat", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(Main.class, argv);
    }
}
