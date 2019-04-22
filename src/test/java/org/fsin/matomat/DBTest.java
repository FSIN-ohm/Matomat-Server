package org.fsin.matomat;

import org.apache.commons.codec.digest.DigestUtils;
import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DBTest {

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

        Random rand;
        rand = new Random();

        // create admin 1
        {
            AdminEntry admin1 = new AdminEntry();
            admin1.setUsername("DER Admin");
            admin1.setEmail("DERadministrator@example.net");

            byte[] salt = new byte[64];
            rand.nextBytes(salt);
            admin1.setPasswordSalt(salt);
            admin1.setPassword(DigestUtils.sha512((new String(salt) + "TestPassword1").getBytes()));

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
            ProductDetailEntry mateDetail = new ProductDetailEntry();
            mateDetail.setName("Mate");
            mate.setPrice(new BigDecimal(2.0));
            mateDetail.setReorderPoint(40);
            mateDetail.setAvailable(true);
            mateDetail.setImageUrl("localhost/404");
            mateDetail.setItemsPerCrate(20);
            mateDetail.setBarcode("f848f929fjiehald");

            db.productAdd(mate, mateDetail);

            ProductEntry cola = new ProductEntry();
            ProductDetailEntry colaDetail = new ProductDetailEntry();
            colaDetail.setName("Kola");
            cola.setPrice(new BigDecimal(1.5));
            colaDetail.setReorderPoint(40);
            colaDetail.setAvailable(true);
            colaDetail.setImageUrl("localhost/404");
            colaDetail.setItemsPerCrate(15);
            colaDetail.setBarcode("ifj48jafighiejaf==");

            db.productAdd(cola, colaDetail);

            ProductEntry bier = new ProductEntry();
            ProductDetailEntry bierDetail = new ProductDetailEntry();
            bierDetail.setName("Bier");
            bier.setPrice(new BigDecimal(2.5));
            bierDetail.setReorderPoint(30);
            bierDetail.setAvailable(true);
            bierDetail.setImageUrl("localhost/404");
            bierDetail.setItemsPerCrate(10);
            bierDetail.setBarcode("afihgeijafsdf3fasdf=");

            db.productAdd(bier, bierDetail);

            ProductEntry snickers = new ProductEntry();
            ProductDetailEntry snickersDetail = new ProductDetailEntry();
            snickersDetail.setName("Schickas");
            snickers.setPrice(new BigDecimal(6.5));
            snickersDetail.setReorderPoint(10);
            snickersDetail.setAvailable(true);
            snickersDetail.setImageUrl("localhost/404");
            snickersDetail.setItemsPerCrate(200);
            snickersDetail.setBarcode("if84hg8498e8jf8j8");

            db.productAdd(snickers, snickersDetail);

        }

        // create user 1
        {
            UserEntry user1 = new UserEntry();
            user1.setAuthHash("user1".getBytes());

            db.userCreate(user1.getAuthHash());

            user1 = db.userAuthenticate( user1.getAuthHash() );

            // deposit
            TransactionEntry deposit = new TransactionEntry();
            deposit.setRecipientId(user1.getId());
            deposit.setAmount(new BigDecimal(10.0));
            db.transactionDeposit(deposit);

            // purchaseEntry
            TransactionEntry purchaseEntry = new TransactionEntry();

            purchaseEntry.setSenderId(user1.getId());

            ProductCountEntry product1 = new ProductCountEntry();
            product1.setProductsId(1);
            product1.setCount(3);

            List<ProductCountEntry> products = new ArrayList<ProductCountEntry>();
            products.add(product1);

            db.transactionPurchase(purchaseEntry, products);
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
            for (AdminEntry admin : db.adminGetAll(1, 100000, true)) {
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
            }

            System.out.println("\nAll Products");
            System.out.println("=======================================");
            System.out.println("ID\tPrice\tName\tReorder\tImage");
            for (ProductEntry product : db.productsGetActive() ) {
                System.out.print(product.getId());
                System.out.print("\t");
                System.out.print(product.getPrice());
                System.out.print("\t");
            }

            System.out.println("\nUsers");
            System.out.println("=======================================");
            System.out.println("ID\tLast Seen\tBalance");
            for (UserEntry user : db.usersGetAll(0, 100000, false) ) {
                System.out.print(user.getId());
                System.out.print("\t");
                System.out.print(user.getLastSeen());
                System.out.print("\t");
                System.out.println(user.getBalance());
            }

            System.out.println("\nTransactions");
            System.out.println("=======================================");
            System.out.println("ID\tDate\t\tSender\tReciever\tTotal");
            for (TransactionEntry transaction : db.transactionsGetAll(1,
                    100,
                    TransactionEntry.TransactionType.ANY) ) {
                System.out.print(transaction.getId());
                System.out.print("\t");
                System.out.print(transaction.getDate());
                System.out.print("\t");
                System.out.print(transaction.getSenderId());
                System.out.print("\t\t");
                System.out.print(transaction.getRecipientId());
                System.out.print("\t\t\t");
                System.out.println(transaction.getAmount());
            }

        }
    }
}
