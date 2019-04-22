package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.ProductDetailEntry;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.ProductInfo;
import org.fsin.matomat.rest.model.UpdateProductInfo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static org.fsin.matomat.rest.Utils.checkRequest;

@RestController
public class ProductsInfoController {

    private ProductInfo mapEntryToProductInfo(ProductDetailEntry entry) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setBarcode(entry.getBarcode());
        productInfo.setId(entry.getId());
        productInfo.setIs_available(entry.isAvailable());
        productInfo.setItems_per_crate(entry.getItemsPerCrate());
        productInfo.setName(entry.getName());
        productInfo.setReorder_point(entry.getReorderPoint());
        productInfo.setThumbnail(entry.getImageUrl());
        return productInfo;
    }

    @RequestMapping("/v1/product_infos")
    public ProductInfo[] getInfos()
        throws Exception {
        Database db = Database.getInstance();
        List<ProductDetailEntry> entries = db.productDetailGetAll();
        ProductInfo productInfo[] = new ProductInfo[entries.size()];
        for(int i = 0; i < productInfo.length; i++) {
            productInfo[i] = mapEntryToProductInfo(entries.get(i));
        }
        return productInfo;
    }

    @RequestMapping("/v1/product_infos/{id}")
    public ProductInfo getInfo(@PathVariable int id)
        throws Exception {
        Database db = Database.getInstance();
        try {
            return mapEntryToProductInfo(db.productDetailGetById(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @PatchMapping("/v1/product_infos/{id}")
    public ResponseEntity patchInfo(@PathVariable int id,
                                    @RequestBody UpdateProductInfo change)
        throws Exception {

        checkRequest(change.getName());
        checkRequest(change.getThumbnail());
        checkRequest(change.getReorder_point());
        checkRequest(change.getItems_per_crate());
        checkRequest(change.getBarcode());

        try {
            Database db = Database.getInstance();
            ProductDetailEntry entry = db.productDetailGetById(id);
            entry.setName(change.getName());
            entry.setImageUrl(change.getThumbnail());
            entry.setReorderPoint(change.getReorder_point());
            entry.setItemsPerCrate(change.getItems_per_crate());
            entry.setBarcode(change.getBarcode());
            entry.setAvailable(change.isIs_available());
            db.productDetailUpdate(entry);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }
}
