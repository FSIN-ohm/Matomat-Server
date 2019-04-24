package org.fsin.matomat.database;

import org.fsin.matomat.database.dao.*;
import org.fsin.matomat.database.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

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

    public List<UserEntry> usersGetAll(int from, int to, boolean onlyAvailable) {
        return new UsersDAO(template).getAll(from, to, onlyAvailable);
    }

    /**
     * Create a user
     * @param authHash the users authentication hash
     **/
    public void userCreate(byte[] authHash){
        UserEntry user = new UserEntry();
        user.setAuthHash(authHash);
        new UsersDAO(template).addUser(user);
    }

    /**
     * Update a user
     * @param authHash the users authentication hash
     **/
    public void userUpdate(int id, byte[] authHash, String name){
        UsersDAO dao = new UsersDAO(template);
        UserEntry entry = dao.getUser(id);
        entry.setAuthHash(authHash);
        entry.setName(name);
        dao.updateUser(entry);
    }

    public void userDelete(int id) {
        UsersDAO dao = new UsersDAO(template);
        UserEntry entry = dao.getUser(id);
        entry.setAvailable(false);
        entry.setAuthHash(generateRandomBinary());
        dao.updateUser(entry);
    }

    /** Authenticate a user by his hash
     * @param authHash the users authentication hash value
     * @return the users id
     */
    public UserEntry userAuthenticate(byte[] authHash) throws Exception {
        return new UsersDAO(template).getUser(authHash);
    }

    public UserEntry getUser(int id) {
        return new UsersDAO(template).getUser(id);
    }

    /* ********** Admins **********/

    public List<AdminEntry> adminGetAll(int from, int to, boolean onlyAvailable) {
        return new AdminDAO(template).getAll(from, to, onlyAvailable);
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
    public AdminEntry adminGetDetail(int id) {
        return new AdminDAO(template).getAdmin(id);
    }

    public void adminUpdate(AdminEntry detail) {
        new AdminDAO(template).updateAdmin(detail);
    }

    /* ********** Products **********/

    public void productAdd(ProductEntry productEntry, PriceEntry priceEntry){
        new ProductDAO(template).addProduct(productEntry, priceEntry);
    }

    /**
     * Change detail of a product
     * @param changedProduct
     */
    public void productUpdate(ProductEntry changedProduct){
        new ProductDAO(template).updateProduct(changedProduct);
    }

    public void productPriceUpdate(ProductEntry product) {
        new ProductDAO(template).updatePrice(product);
    }

    public List<PriceEntry> pricesGetAll() {
        return new PriceDAO(template).getAll();
    }

    public List<ProductEntry> productsGetAll(boolean onlyAvailable) {
        return new ProductDAO(template).getAll(onlyAvailable);
    }

    public ProductEntry productDetailGetById(int id) {
        return new ProductDAO(template).getById(id);
    }

    public PriceEntry productsGetById(int id) {
        return new PriceDAO(template).getById(id);
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

    public void transactionOrder(OrderEntry orderEntry, List<OrderedProductEntry> products){
        new TransactionDAO(template).addOrder(orderEntry, products);
    }

    public List<TransactionEntry> transactionsGetAll(long from, long to, TransactionEntry.TransactionType type){
        return new TransactionDAO(template).getAll(from, to, type);
    }

    public List<TransactionEntry> transactionsGetAll(long from, long to, TransactionEntry.TransactionType type, int user){
        return new TransactionDAO(template).getAll(from, to, type, user);
    }

    public TransactionEntry transactionGet(long id) {
        return new TransactionDAO(template).getTransaction(id);
    }

    public List<ProductCountEntry> purchaseGetProducts(TransactionEntry purchase) {
        return new ProductCountDAO(template).getByPurchase(purchase);
    }

    public OrderEntry orderGet(long id) {
        return new OrderDAO(template).getOrder(id);
    }

    public List<OrderedProductEntry> orderGetProducts(OrderEntry order) {
        return new OrderDAO(template).getOrderedProducts(order);
    }

    public JdbcTemplate getTemplate() {
        return template;
    }


    /***************** UTILS **************************/

    private byte[] generateRandomBinary() {
        byte[] array = new byte[20]; // length is bounded by 7
        new Random().nextBytes(array);
        return array;
    }
}
