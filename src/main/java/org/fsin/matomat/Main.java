package org.fsin.matomat;

import org.fsin.matomat.database.Database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Main {
    public static void main(String[] argv) {

        Database db;

        try{
            db = new Database("127.0.0.1", "matohmat", "root", "root");

            TestScenario test = new TestScenario(db);
            test.run();

        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(Main.class, argv);
    }

    @RestController
    class HalloWelt {
        @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
        public ResponseEntity<String> hello() {
            return new ResponseEntity<String>("Hello World!", HttpStatus.OK);
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