package org.fsin.matomat;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TestScenario {

    static Database db;

    @BeforeClass
    public static void setupClass() throws Exception {
        Database.init("127.0.0.1", "matohmat", "root", "root");
        db = Database.getInstance();
    }


    @Test
    public void thisIsAVerryWrongTest() throws Exception {
        /**
         * Story:
         * Create admin 1
         * authenticate admin 1
         * Add Mate
         * Add Cola
         * Add bier
         * Add Snikaz
         * create user 1
         * authenticate user 1
         * deposit money 1
         * buy mate
         *
         * create user 2
         * authenticate user 2
         * deposit moneyz 2
         * buy bier and snikas
         * create admin 2
         * authenticate admin 2
         * change bier price
         * change snicers price
         * authenticate user 1
         * buy mate
         * authenticate admin 1
         * disable bier // EVIL alcohol >:)
         * authenticate user 2
         * buy everyting twice
         * display admins
         * display users
         * display products
         * display transactions
         * display transaction interleaved with their products
         */

        MessageDigest sha1;
        Random rand;

        sha1 = MessageDigest.getInstance("SHA1");
        rand = new Random();

        // create admin 1
        {
            AdminEntry admin1 = new AdminEntry();
            admin1.setUsername("DER Admin");
            admin1.setEmail("DERadministrator@example.net");

            byte[] salt = new byte[20];
            rand.nextBytes(salt);
            admin1.setPasswordSalt(new String(salt));
            admin1.setPassword(new String(sha1.digest((new String(salt) + "TestPassword1").getBytes())));

            db.adminCreate(admin1);

        }

        // authenticate admin 1
        {
            AdminEntry admin1 = new AdminEntry();
            admin1.setUsername("DER Admin");


            int admin1id = db.adminAuthenticate(admin1);
            admin1 = db.adminGetDetail(admin1id);
        }

        // add mate, cola and bier
        {
            ProductEntry mate = new ProductEntry();
            mate.setName("Mate");
            mate.setPrice(new BigDecimal(2.0));
            mate.setReorderPoint(40);
            mate.setAvailable(true);
            mate.setImageUrl("localhost/404");
            mate.setItemsPerCrate(20);
            mate.setBarcode("f848f929fjiehald");

            db.productAdd(mate);

            ProductEntry cola = new ProductEntry();
            cola.setName("Kola");
            cola.setPrice(new BigDecimal(1.5));
            cola.setReorderPoint(40);
            cola.setAvailable(true);
            cola.setImageUrl("localhost/404");
            cola.setItemsPerCrate(15);
            cola.setBarcode("ifj48jafighiejaf==");

            db.productAdd(cola);

            ProductEntry bier = new ProductEntry();
            bier.setName("Bier");
            bier.setPrice(new BigDecimal(2.5));
            bier.setReorderPoint(30);
            bier.setAvailable(true);
            bier.setImageUrl("localhost/404");
            bier.setItemsPerCrate(10);
            bier.setBarcode("afihgeijafsdf3fasdf=");

            db.productAdd(bier);

            ProductEntry snickers = new ProductEntry();
            snickers.setName("Schickas");
            snickers.setPrice(new BigDecimal(6.5));
            snickers.setReorderPoint(10);
            snickers.setAvailable(true);
            snickers.setImageUrl("localhost/404");
            snickers.setItemsPerCrate(200);
            snickers.setBarcode("if84hg8498e8jf8j8");

            db.productAdd(snickers);

        }

        // create user 1
        {
            UserEntry user1 = new UserEntry();
            user1.setAuthHash("user1".getBytes());

            db.userCreate(user1.getAuthHash());

            user1 = db.userAuthenticate( user1.getAuthHash() );

            // deposit
            TransferEntry deposit = new TransferEntry();
            deposit.setRecipientId(user1.getId());
            deposit.setCharged_amount(new BigDecimal(10.0));
            db.transactionDeposit(deposit);

            // purchase
            Purchase purchase = new Purchase();

            purchase.setSenderId(user1.getId());

            PurchaseEntry product1 = new PurchaseEntry();
            product1.setProductsId(1);
            product1.setCount(3);

            List<PurchaseEntry> products = new ArrayList<PurchaseEntry>();
            products.add(product1);
            purchase.setProducts(products);

            db.transactionPurchase(purchase);
        }

        // create user 2
        {
            // authenticate

            // buy bier ans scnickers
        }

        // create admin 2
        {
            // authenticate

            // change price ( bier and snickers )
        }

        // authenticate user 1
        {

            // buy mate
        }

        // authenticate admin
        {
            // disable bier
        }

        // authenticate user 2
        {
            // buy everything twice
        }

        {
            System.out.println("\nAdmins");
            System.out.println("=======================================");
            System.out.println("ID\tUsername\teMail\t\tUserID");
            for (AdminEntry admin : db.adminGetAll() ) {
                System.out.print(admin.getId());
                System.out.print("\t");
                System.out.print(admin.getUsername());
                System.out.print("\t");
                System.out.print(admin.getEmail());
                System.out.print("\t");
                System.out.println(admin.getCorespondingUserId());
            }

            System.out.println("\nAll Products");
            System.out.println("=======================================");
            System.out.println("ID\tPrice\tName\tReorder\tVerfügbar\tImage");
            for (ProductEntry product : db.productsGetAll() ) {
                System.out.print(product.getId());
                System.out.print("\t");
                System.out.print(product.getPrice());
                System.out.print("\t");
                System.out.print(product.getName());
                System.out.print("\t");
                System.out.print(product.getReorderPoint());
                System.out.print("\t");
                System.out.print(product.isAvailable() ? "Ja":"Nein");
                System.out.print("\t");
                System.out.println(product.getImageUrl());
            }

            System.out.println("\nAll Products");
            System.out.println("=======================================");
            System.out.println("ID\tPrice\tName\tReorder\tImage");
            for (ProductEntry product : db.productsGetActive() ) {
                System.out.print(product.getId());
                System.out.print("\t");
                System.out.print(product.getPrice());
                System.out.print("\t");
                System.out.print(product.getName());
                System.out.print("\t");
                System.out.print(product.getReorderPoint());
                System.out.print("\t\t");
                System.out.println(product.getImageUrl());
            }

            System.out.println("\nUsers");
            System.out.println("=======================================");
            System.out.println("ID\tLast Seen\tBalance");
            for (UserEntry user : db.usersGetAll() ) {
                System.out.print(user.getId());
                System.out.print("\t");
                System.out.print(user.getLastSeen());
                System.out.print("\t");
                System.out.println(user.getBalance());
            }

            System.out.println("\nTransactions");
            System.out.println("=======================================");
            System.out.println("ID\tDate\t\tSender\tReciever\tTotal");
            for (TransactionEntry transaction : db.transactionsGetAll() ) {
                System.out.print(transaction.getId());
                System.out.print("\t");
                System.out.print(transaction.getDate());
                System.out.print("\t");
                System.out.print(transaction.getSenderId());
                System.out.print("\t\t");
                System.out.print(transaction.getRecipientId());
                System.out.print("\t\t\t");
                System.out.println(transaction.getAmount());

                for (PurchaseEntry product : db.transactionGetProducts(transaction.getId() ) ) {
                    System.out.println("\t\t----------------------------------------");
                    System.out.println("\t\t|ID\tName\t\tPrice\tCount\tImege Url");
                    System.out.print("\t\t|");
                    System.out.print(product.getProductsId());
                    System.out.print("\t");
                    System.out.print(product.getName());
                    System.out.print("\t\t");
                    System.out.print(product.getPrice());
                    System.out.print("\t");
                    System.out.print(product.getCount());
                    System.out.print("\t");
                    System.out.println(product.getImage_url());
                    System.out.println("\t\t----------------------------------------");
                }
            }


            //db.transactionsGetAll();

            // list all the things
        }
    }
}
