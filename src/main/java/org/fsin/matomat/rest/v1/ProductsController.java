package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.PriceEntry;
import org.fsin.matomat.database.model.ProductEntry;
import org.fsin.matomat.rest.exceptions.AlreadyExistsException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.CreateProduct;
import org.fsin.matomat.rest.model.Product;
import org.fsin.matomat.rest.model.UpdateProduct;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.fsin.matomat.rest.Utils.*;

@RestController
public class ProductsController {

    private Product mapEntryToProductInfo(ProductEntry entry) {
        Product product = new Product();
        product.setBarcode(entry.getBarcode());
        product.setId(entry.getId());
        product.setIs_available(entry.isAvailable());
        product.setItems_per_crate(entry.getItemsPerCrate());
        product.setName(entry.getName());
        product.setReorder_point(entry.getReorderPoint());
        product.setThumbnail(entry.getImageUrl());
        product.setStock(entry.getStock());
        product.setValid_date(entry.getValid_date().toLocalDateTime());
        product.setPrice((int)(entry.getPrice().doubleValue()*100.00));
        return product;
    }

    @RequestMapping("/v1/products")
    public Product[] getInfos(@RequestParam(value="onlyAvailable", defaultValue="true") boolean onlyAvailable)
        throws Exception {
        Database db = Database.getInstance();
        List<ProductEntry> entries = db.productsGetAll(onlyAvailable);
        Product product[] = new Product[entries.size()];
        for(int i = 0; i < product.length; i++) {
            product[i] = mapEntryToProductInfo(entries.get(i));
        }
        return product;
    }

    @RequestMapping("/v1/products/{id}")
    public Product getInfo(@PathVariable int id)
        throws Exception {
        Database db = Database.getInstance();
        try {
            return mapEntryToProductInfo(db.productDetailGetById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @PostMapping("/v1/products")
    public ResponseEntity addProduct(@RequestBody CreateProduct productAdd)
            throws Exception {

        checkIfNotNull(productAdd.getBarcode());
        checkIfNotNull(productAdd.getItems_per_crate());
        checkIfNotNull(productAdd.getName());
        checkIfBelowZero(productAdd.getPrice());
        checkIfBelowZero(productAdd.getReorder_point());
        checkIfNotNull(productAdd.getThumbnail());

        try {
            Database db = Database.getInstance();
            PriceEntry priceEntry = new PriceEntry();
            priceEntry.setPrice(new BigDecimal((int)(productAdd.getPrice()/100.00)));

            ProductEntry productEntry = new ProductEntry();
            productEntry.setBarcode(productAdd.getBarcode());
            productEntry.setName(productAdd.getName());
            productEntry.setItemsPerCrate(productAdd.getItems_per_crate());
            productEntry.setReorderPoint(productAdd.getReorder_point());
            productEntry.setImageUrl(productAdd.getThumbnail());

            db.productAdd(productEntry, priceEntry);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new AlreadyExistsException();
        }
    }

    @PatchMapping("/v1/products/{id}")
    public ResponseEntity patchInfo(@PathVariable int id,
                                    @RequestBody UpdateProduct change)
        throws Exception {

        checkIfNotNull(change.getName());
        checkIfNotNull(change.getThumbnail());
        checkIfBelowZero(change.getReorder_point());
        checkIfBelowZero(change.getItems_per_crate());
        checkIfNotNull(change.getBarcode());
        checkIfBelowZero(change.getPrice());

        try {
            Database db = Database.getInstance();
            ProductEntry entry = db.productDetailGetById(id);


            entry.setName(change.getName());
            entry.setImageUrl(change.getThumbnail());
            entry.setReorderPoint(change.getReorder_point());
            entry.setItemsPerCrate(change.getItems_per_crate());
            entry.setBarcode(change.getBarcode());
            entry.setAvailable(change.isIs_available());
            db.productUpdate(entry);

            /* check if price should be changed */
            if((int)(entry.getPrice().doubleValue()*100.00) != change.getPrice()) {
                entry.setPrice(new BigDecimal(change.getPrice()/100.00));
                db.productPriceUpdate(entry);
            }

            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }
}
