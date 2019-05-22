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
    static String[] origins = new String[]{};

    /**
     * In order to develop and test the server you will want to put certain parameters in the run configuration:
     * 127.0.0.1 matohmat matomat_system password_here device_keys.txt null
     * @param argv for debuging should be "127.0.0.1 matohmat matomat_system password_here device_keys.txt null /"
     */
    public static void main(String[] argv) {
        if(argv.length < 6) {
            System.err.println("Server can not start up as not all required parameters are given. \n" +
                    "The Parameters have to be: \n" +
                    "<ip/host name of db> <db schema> <db user> <db password> <device_kes_file> <CORS origin>");
            System.exit(1);
        }
        final String dbHost = argv[0];
        final String schema = argv[1];
        final String dbUser = argv[2];
        final String dbPwd = argv[3];
        final String deviceKeys = argv[4];
        final String contextPath = argv[5];
        origins = argv[5].split("::");

        try{
            //Database.init("127.0.0.1", "matohmat", "matomat_system", "password_here");
            Database.init(dbHost, schema, dbUser, dbPwd);
            Authenticator.init(deviceKeys);
        } catch (Exception e) {
            e.printStackTrace();
            //quit if we can not reach the database. let docker restart the server
            System.exit(1);
        }

        System.setProperty("server.servlet.context-path", contextPath);
        SpringApplication.run(Main.class, argv);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PATCH");
            }
        };
    }
}
