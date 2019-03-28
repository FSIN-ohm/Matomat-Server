package org.fsin.matomat.rest.model;

public class Product {
    private int id;
    private String name;
    private int price;
    private String thumbnail;
    private int reorder_point;
    private String barcode;
    private boolean is_available;
    private int items_per_crate;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getReorder_point() {
        return reorder_point;
    }

    public void setReorder_point(int reorder_point) {
        this.reorder_point = reorder_point;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public int getItems_per_crate() {
        return items_per_crate;
    }

    public void setItems_per_crate(int items_per_crate) {
        this.items_per_crate = items_per_crate;
    }
}
