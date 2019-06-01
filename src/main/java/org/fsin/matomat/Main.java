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
        final String configFile;
        if(argv.length != 1) {
            System.err.println("In order to start the server you need to provide a location for the config file.");
            configFile = "./server.conf";
        } else {
            configFile = argv[0];
        }

        try{
            Configurator.init(configFile);
            Configurator conf = Configurator.getInstance();
            final String dbHost = conf.getValueString("db_host");
            final String schema = conf.getValueString("db_schema");
            final String dbUser = conf.getValueString("db_user");
            final String dbPwd = conf.getValueString("db_password");
            final String deviceKeys = conf.getValueString("device_keys_file");
            origins = conf.getValueString("origin").split(";;");
            final String contextPath = conf.getValueString("context_path");
            Database.init(dbHost, schema, dbUser, dbPwd);
            Authenticator.init(deviceKeys);

            System.setProperty("server.servlet.context-path", contextPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not init server. Maybe the configuration is not correct.");
            //quit if we can not reach the database. let docker restart the server
            System.exit(1);
        }

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
