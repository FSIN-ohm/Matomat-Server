package org.fsin.matomat.database.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProductEntry {
    private int id;
    private String name;
    private String imageUrl;
    private int reorderPoint;
    private boolean available;
    private String barcode;
    private int itemsPerCrate;
    private int stock;
    private Timestamp valid_date;
    private BigDecimal price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getItemsPerCrate() {
        return itemsPerCrate;
    }

    public void setItemsPerCrate(int itemsPerCrate) {
        this.itemsPerCrate = itemsPerCrate;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Timestamp getValid_date() {
        return valid_date;
    }

    public void setValidDate(Timestamp valid_date) {
        this.valid_date = valid_date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
