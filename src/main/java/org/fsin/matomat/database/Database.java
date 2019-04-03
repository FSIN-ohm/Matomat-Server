package org.fsin.matomat.database;

import org.fsin.matomat.database.dao.*;
import org.fsin.matomat.database.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.util.List;

public class Database {

    private JdbcTemplate template;
    private Connection connection;
    private static Database db = null;

    public static void init(String host, String schema, String username, String password) {
        db = new Database(host, schema, username, password);
    }

    public static Database getInstance() throws Exception {
        if(db == null) {
            throw new Exception("Database not initialized");
        } else {
            return db;
        }
    }

    private Database(String host, String schema, String username, String password){

            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://" + host + "/" + schema);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            template = new JdbcTemplate(dataSource);
    }

    /* ********** Users **********/

    /**
     * Create a user
     * @param authHash the users authentication hash
     **/
    public void userCreate(byte[] authHash){
        UserEntry user = new UserEntry();
        user.setAuthHash(authHash);
        new UsersDAO(template).addUser(user);
    }

    /** Authenticate a user by his hash
     * If the user does not exist it is created
     * @param authHash the users authentication hash value
     * @return the users id
     */
    public UserEntry userAuthenticate(byte[] authHash){

        UserEntry user = null;

        try {
            user = new UsersDAO(template).getUser(authHash);
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    /* ********** Admins **********/

    public List<AdminEntry> adminGetAll() {
        return new AdminDAO(template).getAll();
    }

    /**
     * Add Admin
     * @param detail details of the admin
     */
    public void adminCreate(AdminEntry detail){
        new AdminDAO(template).addAdmin(detail);
    }

    /**
     * get the neccessary details, to authenticate an admin
     * @param detail Admin object containing at least the username
     * @return Admin object containing all information of the Admin
     */
    public int adminAuthenticate(AdminEntry detail){
        detail = new AdminDAO(template).getAdmin(detail.getUsername());
        if(detail != null){
            return detail.getId();
        } else {
            return -1;
        }
    }

    /**
     * get public detail for an admin
     * does not contain authentication information
     * @param id id of the admin
     * @return details for the admin
     */
    public AdminEntry adminGetDetail(int id){ return null; }

    /* ********** Products **********/

    /**
     * add a new product
     * @param product details for the product
     */
    public void productAdd(ProductEntry product){
        new ProductDAO(template).addProduct(product);
    }

    /**
     * Change detail of a product
     * Checks if the the price or other details have changed and performs the appropriate action.
     * @param changedProduct
     */
    public void productUpdate(ProductEntry changedProduct){
        ProductEntry currentProduct = productGetDetail(changedProduct.getId());
        if(changedProduct.getPrice() == currentProduct.getPrice()){
            new ProductDAO(template).updatePrice(changedProduct);
        }
        new ProductDAO(template).updateProduct(changedProduct);
    }

    /**
     * Get all existing products
     * Includes inactive products
     * @return List of products
     */
    public List<ProductEntry> productsGetAll() {
        return new ProductDAO(template).getAll();
    }

    /**
     * Get All products currently on offer
     * Excludes all inactive products
     * @return List of products
     */
    public List<ProductEntry> productsGetActive(){
        return new ProductDAO(template).getActive();
    }

    /**
     * Get detail for a Product
     * @param id id of the product
     * @return Detail of the product
     */
    public ProductEntry productGetDetail(int id){
        return new ProductDAO(template).getDetail(id);
    }

    /* ********** Transactions **********/


    public void transactionPurchase(TransactionEntry purchaseEntry, List<ProductCountEntry> products){
        new TransactionDAO(template).addPurchase(purchaseEntry, products);
    }

    public void transactionDeposit(TransactionEntry deposit){
        new TransactionDAO(template).addDeposit(deposit);
    }

    public void transactionWithdraw(TransactionEntry withdraw){
        new TransactionDAO(template).addWithdraw(withdraw);
    }

    public void transactionTransfer(TransactionEntry transfer){
        new TransactionDAO(template).addTransfer(transfer);
    }

    public void transactionOrder(OrderEntry orderEntry, List<ProductCountEntry> products){
        new TransactionDAO(template).addOrder(orderEntry, products);
    }

    public List<TransactionEntry> transactionsGetAll(){
        return new TransactionDAO(template).getAll();
    }

    public List<UserEntry> usersGetAll() { return new UsersDAO(template).getAll();
    }

    public JdbcTemplate getTemplate() {
        return template;
    }
}
