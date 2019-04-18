package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.ProductDetailEntry;
import org.fsin.matomat.database.model.ProductEntry;
import org.fsin.matomat.rest.exceptions.AlreadyExistsException;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.Product;
import org.fsin.matomat.rest.model.ProductAdd;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ProductsController {

    private Product mapEntryToProduct(ProductEntry entry) {
        Product product = new Product();
        product.setPrice(entry.getPrice().intValue()*100);
        product.setId(entry.getId());
        product.setInfo_id(entry.getProductDetailId());
        product.setValid_from(entry.getValidFrom().toLocalDateTime());
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

    @RequestMapping("/v1/products/{id}")
    public Product product(@PathVariable int id)
        throws Exception {
        Database db = Database.getInstance();
        try {
            return mapEntryToProduct(db.productsGetById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @PostMapping("/v1/products")
    public ResponseEntity addProduct(@RequestBody ProductAdd productAdd)
        throws Exception {

        checkRequest(productAdd.getBarcode());
        checkRequest(productAdd.getItems_per_crate());
        checkRequest(productAdd.getName());
        checkRequest(productAdd.getPrice());
        checkRequest(productAdd.getReorder_point());
        checkRequest(productAdd.getThumbnail());

        try {
            Database db = Database.getInstance();
            ProductEntry productEntry = new ProductEntry();
            productEntry.setPrice(new BigDecimal(productAdd.getPrice()/100));
            ProductDetailEntry productDetailEntry = new ProductDetailEntry();
            productDetailEntry.setBarcode(productAdd.getBarcode());
            productDetailEntry.setName(productAdd.getName());
            productDetailEntry.setItemsPerCrate(productAdd.getItems_per_crate());
            productDetailEntry.setReorderPoint(productAdd.getReorder_point());
            productDetailEntry.setImageUrl(productAdd.getThumbnail());
            db.productAdd(productEntry, productDetailEntry);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new AlreadyExistsException();
        }
    }

    /***************** UTILS **************************/

    private void checkRequest(Object object) throws BadRequestException {
        if(object == null) {
            throw new BadRequestException();
        }
    }
}
