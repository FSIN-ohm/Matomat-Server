package org.fsin.matomat.rest;

import org.fsin.matomat.rest.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class V1Controller {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("<html>This is the Matohmat REST intorface. "
                + "Please read in the <a href=\"https://fsin-ohm.github.io/Matomat-Documentation/\">Documentation</a> "+
                "how to use this.</html>", HttpStatus.OK);
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    //================= User =======================

    @RequestMapping("/v1/users")
    public User[] users() {
        return new User[]{};
    }

    //================= User =======================

    @RequestMapping("/v1/admins")
    public Admin[] admins() {
        return new Admin[]{};
    }

    //================= User =======================

    @RequestMapping("/v1/products")
    public Product[] products() {
        return new Product[]{};
    }

    //================= User =======================

    @RequestMapping("/v1/transactions")
    public Transaction[] transactions() {
        Purchase purchase = new Purchase();
        Product products[] = new Product[] {
                new Product(),
                new Product()
        };
        purchase.setProducts(products);
        Transfare transfare = new Transfare();
        transfare.setAmount(400);
        Transaction[] transactions = new Transaction[] {
                purchase,
                transfare
        };

        return transactions;
    }
}
