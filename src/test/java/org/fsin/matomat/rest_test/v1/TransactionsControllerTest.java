package org.fsin.matomat.rest_test.v1;

import org.junit.BeforeClass;

public class TransactionsControllerTest {
    WebTestClient client;

    @BeforeClass
    void init() {
        client = WebTestClient.bindToController(new TestController()).build();
    }
}
