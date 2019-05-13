package org.fsin.matomat;

import org.fsin.matomat.database.Database;

import org.fsin.matomat.rest.auth.Authenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Main {
    String origin = "";

    public static void main(String[] argv) {
        if(argv.length < 6) {
            System.err.println("Server can not start up as not all required parameters are given. \n" +
                    "The Parameters have to be: \n" +
                    "<ip/host name of db> <db schema> <db user> <db password> <device_kes_file> <CORS origin>");
            System.exit(1);
        }
        String dbHost = argv[0];
        String schema = argv[1];
        String dbUser = argv[2];
        String dbPwd = argv[3];
        String deviceKeys = argv[4];
        String origin = argv[5];

        try{
            Database.init("127.0.0.1", "matohmat", "matomat_system", "password_here");
            //Database.init(dbHost, schema, dbUser, dbPwd);
            Authenticator.init(deviceKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(Main.class, argv);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //registry.addMapping("/").allowedOrigins(origin);
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080", "null")
                        .allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PATCH");
            }
        };
    }
}
