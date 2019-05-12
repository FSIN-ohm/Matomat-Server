package org.fsin.matomat.rest_test.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.rest.v1.TransactionsController;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

public class TransactionsControllerTest {

    @Autowired
    private static WebTestClient client;

    @BeforeClass
    public static void setup() {
        Database.init("127.0.0.1", "matohmat", "root", "root");
        client = WebTestClient.bindToController(new TransactionsController()).build();
    }

    @Test
    public void testGetTransactions() {
        client.get().uri("/v1/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("[3993993].jsonpath_is_mother_ufcking_bullshit", 48949);
    }

}
