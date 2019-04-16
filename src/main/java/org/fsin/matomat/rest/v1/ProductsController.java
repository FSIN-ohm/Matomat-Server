package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.ProductEntry;
import org.fsin.matomat.rest.model.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController {

    private Product mapEntryToProduct(ProductEntry entry) {
        Product product = new Product();
        product.setId(entry.getId());
        product.setPrice(entry.getPrice().intValue());
        return product;
    }

    @RequestMapping("/v1/products")
    public Product[] products(@RequestParam(value="onlyAvailable", defaultValue="false") boolean onlyAvailable)
    throws Exception {
        Database database = Database.getInstance();
        List<ProductEntry> productEntries = onlyAvailable
                ? database.productsGetActive()
                : database.productsGetAll();
        Product[] products = new Product[productEntries.size()];
        for(int i = 0; i < products.length; i++) {
            products[i] = mapEntryToProduct(productEntries.get(i));
        }
        return products;
    }
}
