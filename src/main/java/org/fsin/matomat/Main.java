package org.fsin.matomat;

import org.fsin.matomat.database.Database;

import org.fsin.matomat.rest.auth.Authenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] argv) {

        if(argv.length < 5) {
            System.err.println("Server can not start up as not all required parameters are given. \n" +
                    "The Parameters have to be: \n" +
                    "<ip/host name of db> <db schema> <db user> <db password> <device_kes_file>");
            System.exit(1);
        }
        String dbHost = argv[0];
        String schema = argv[1];
        String dbUser = argv[2];
        String dbPwd = argv[3];
        String deviceKeys = argv[4];

        try{
            //Database.init("127.0.0.1", "matohmat", "matomat_system", "<password_here>");
            Database.init(dbHost, schema, dbUser, dbPwd);
            Authenticator.init(deviceKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(Main.class, argv);
    }
}
