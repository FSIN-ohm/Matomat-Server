package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.ProductDetailEntry;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.ProductInfo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
