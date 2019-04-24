package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.ProductEntry;
import org.fsin.matomat.database.model.PriceEntry;
import org.fsin.matomat.rest.exceptions.AlreadyExistsException;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.Price;
import org.fsin.matomat.rest.model.CreateProduct;
import org.fsin.matomat.rest.model.UpdatePrice;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.fsin.matomat.rest.Utils.checkIfBelowZero;

@RestController
public class PricesController {

    private Price mapEntryToPrice(PriceEntry entry) {
        Price price = new Price();
        price.setPrice((int)(entry.getPrice().doubleValue()*100.00));
        price.setId(entry.getId());
        price.setInfo_id(entry.getProductDetailId());
        price.setValid_from(entry.getValidFrom().toLocalDateTime());
        return price;
    }

    /*

    @RequestMapping("/v1/products")
    public Price[] products(@RequestParam(value="onlyAvailable", defaultValue="false") boolean onlyAvailable)
        throws Exception {
        Database database = Database.getInstance();
        List<PriceEntry> productEntries = onlyAvailable
                ? database.productsGetActive()
                : database.productsGetAll();
        Price[] prices = new Price[productEntries.size()];
        for(int i = 0; i < prices.length; i++) {
            prices[i] = mapEntryToPrice(productEntries.get(i));
        }
        return prices;
    }

    @RequestMapping("/v1/products/{id}")
    public Price product(@PathVariable int id)
        throws Exception {
        Database db = Database.getInstance();
        try {
            return mapEntryToPrice(db.productsGetById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @PostMapping("/v1/products")
    public ResponseEntity addProduct(@RequestBody CreateProduct productAdd)
        throws Exception {

        checkRequest(productAdd.getBarcode());
        checkRequest(productAdd.getItems_per_crate());
        checkRequest(productAdd.getName());
        checkIfBelowZero(productAdd.getPrice());
        checkIfBelowZero(productAdd.getReorder_point());
        checkRequest(productAdd.getThumbnail());

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
    public ResponseEntity updatePrice(@PathVariable int id, @RequestBody UpdatePrice updatePrice)
        throws Exception {
        checkIfBelowZero(updatePrice.getPrice());

        Database db = Database.getInstance();
        PriceEntry entry = new PriceEntry();
        entry.setId(id);
        entry.setPrice(new BigDecimal(updatePrice.getPrice()/100.00));
        db.productPriceUpdate(entry);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
    */
}
