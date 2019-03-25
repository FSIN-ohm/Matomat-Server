package org.fsin.matomat.database.model;

import java.math.BigDecimal;

public class ProductEntry {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Integer reorderPoint;
    private boolean isAvailable;
    private String barcode;
    private Integer itemsPerCrate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getItemsPerCrate() {
        return itemsPerCrate;
    }

    public void setItemsPerCrate(Integer itemsPerCrate) {
        this.itemsPerCrate = itemsPerCrate;
    }
}
