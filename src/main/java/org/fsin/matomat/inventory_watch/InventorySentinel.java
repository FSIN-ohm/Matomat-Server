package org.fsin.matomat.inventory_watch;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.ProductEntry;

import java.util.HashMap;
import java.util.List;

public class InventorySentinel {
    private static volatile InventorySentinel inventorySentinel = null;

    private HashMap<Integer, ProductEntry> lastCheckt;

    private InventorySentinel() {
    }

    public static InventorySentinel getInstance() {
        if(inventorySentinel == null) {
            inventorySentinel = new InventorySentinel();
        }
        return inventorySentinel;
    }

    public void checkInventory() throws Exception {
        Database db = Database.getInstance();
        List<ProductEntry> products = db.productsGetAll(true);
        boolean detected = false;
        for(int i = 0; i < products.size() && !detected; i++) {
            ProductEntry product = products.get(i);
            if(product.getStock() <= product.getReorderPoint()) {
                // in order to send a mail only once we may want to check if the mail hast not been send
                // during the last check. We do this by checking weather stock was below reorder point
                // at the last check as well
                ProductEntry lastEntry = lastCheckt.get(product.getId());
                if(lastEntry.getStock() > lastEntry.getReorderPoint()) {
                    detected = true;
                    sendNotification(product, products);
                }
            }
        }

        lastCheckt.clear();
        for(ProductEntry product : products) {
            lastCheckt.put(product.getId(), product);
        }

    }

    private void sendNotification(ProductEntry detectedProduct, List<ProductEntry> products) throws Exception {
        Mailer mailer = new Mailer();
        // We have no intelligence here. That functionality is left to be implemented.
        // Because this function is not in there we simply print out which crates are still left.
        Database db = Database.getInstance();
        Mailer.CratesPerProduct[] cratesPerProducts = new Mailer.CratesPerProduct[products.size()];
        for(int i = 0; i < products.size(); i++) {
            ProductEntry product = products.get(i);
            cratesPerProducts[i] = new
                    Mailer.CratesPerProduct(product.getName(),
                    product.getStock()/product.getItemsPerCrate());
        }

        mailer.sendReorderMessage(detectedProduct.getName(), cratesPerProducts);
    }
}
