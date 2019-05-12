package org.fsin.matomat;

import org.fsin.matomat.database.Database;

import org.fsin.matomat.rest.auth.Authenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication

public class Main {
    public static void main(String[] argv) {

        try{
            Database.init("127.0.0.1", "matohmat", "root", "root");
            Authenticator.init("./device_keys.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(Main.class, argv);
    }
}
