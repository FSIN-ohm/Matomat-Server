package org.fsin.matomat.rest.v1;

import org.fsin.matomat.rest.model.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {
    @RequestMapping("/v1/products")
    public Product[] products(@RequestParam(value="count", defaultValue="-1") int count,
                              @RequestParam(value="page", defaultValue="0") int page,
                              @RequestParam(value="onlyAvailable", defaultValue="false") boolean onlyAvailable) {
        return new Product[]{};
    }

}
