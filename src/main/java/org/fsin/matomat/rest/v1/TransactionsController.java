package org.fsin.matomat.rest.v1;

import org.fsin.matomat.rest.model.Product;
import org.fsin.matomat.rest.model.Purchase;
import org.fsin.matomat.rest.model.Transaction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionsController {
    @RequestMapping("/v1/transactions")
    public Transaction[] transactions(@RequestParam(value="count", defaultValue="-1") int count,
                                      @RequestParam(value="page", defaultValue="0") int page,
                                      @RequestParam(value="user", defaultValue="") String user,
                                      @RequestParam(value="type", defaultValue="") String value) {
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
