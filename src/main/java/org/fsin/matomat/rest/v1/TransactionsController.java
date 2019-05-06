package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.dao.ProductBoughtDAO;
import org.fsin.matomat.database.dao.TransactionDAO;
import org.fsin.matomat.database.model.*;
import org.fsin.matomat.rest.auth.User;
import org.fsin.matomat.rest.auth.UserPwdTocken;
import org.fsin.matomat.rest.exceptions.AccessDeniedException;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.fsin.matomat.rest.Utils.checkIfBelowOne;
import static org.fsin.matomat.rest.Utils.checkIfNotNull;


@RestController
public class TransactionsController {

    private Transaction mapSimpleTransaction(TransactionEntry entry) {
        Transaction transaction = new Transaction();
        transaction.setDate(entry.getDate());
        transaction.setAmount((int)(entry.getAmount().doubleValue()*100.00));
        transaction.setId(entry.getId());
        transaction.setReceiver(entry.getRecipientId());
        transaction.setSender(entry.getSenderId());
        transaction.setTransaction_type(entry.getType().value());
        return transaction;
    }

    private OrderedProduct mapOrderedProduct(OrderedProductEntry entry) {
        OrderedProduct product = new OrderedProduct();
        product.setAmount(entry.getCount());
        product.setProduct_info(entry.getProductId());
        return product;
    }

    private ProductCountPrice mapProductCountPrice(ProductBoughtEntry entry) {
        ProductCountPrice price = new ProductCountPrice();
        price.setAmount(entry.getCount());
        price.setPrice_per_unit((int)(entry.getUnitPrice().doubleValue()*100.00));
        price.setProduct(entry.getProductId());
        return price;
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping("/v1/transactions")
    public Transaction[] transactions(@RequestParam(value="count", defaultValue="-1") int count,
                                      @RequestParam(value="page", defaultValue="0") int page,
                                      @RequestParam(value="user", defaultValue="-1") int user,
                                      @RequestParam(value="type", defaultValue="") String typeRaw,
                                      @AuthenticationPrincipal UserPwdTocken userToken)
        throws Exception {

        if(userToken.getRole() != User.Role.ADMIN) {
            if(user != -1)
                throw new AccessDeniedException();
            else
                user = userToken.getId();
        }

        try {
            TransactionEntry.TransactionType type = TransactionDAO.getTypeFromString(typeRaw);
            Database db = Database.getInstance();
            List<TransactionEntry> transactionEntries = user == -1
                    ? (count == -1
                        ? db.transactionsGetAll(1, 100, type)
                        : db.transactionsGetAll(1+count*page, 1+count*(page+1), type))
                    : (count == -1
                        ? db.transactionsGetAll(1, 100, type, user)
                        : db.transactionsGetAll(1+count*page, 1+count*(page+1), type, user));
            Transaction[] transactions = new Transaction[transactionEntries.size()];
            for(int i = 0; i < transactions.length; i++) {
                transactions[i] = mapSimpleTransaction(transactionEntries.get(i));
            }
            return transactions;
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        } catch (TransactionDAO.UnknownTransactionTypeException e) {
            throw new BadRequestException();
        }
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping("/v1/transactions/{id}")
    public Transaction transaction(@PathVariable long id,
                                   @AuthenticationPrincipal UserPwdTocken userToken)
        throws Exception {
        try {
            Database db = Database.getInstance();
            TransactionEntry entry = db.transactionGet(id);

            //check if user is authorised to access this data
            if(userToken.getRole() != User.Role.ADMIN
                    && entry.getSenderId() != userToken.getId()
                    && entry.getRecipientId() != userToken.getId()) {
                throw new ResourceNotFoundException();
                // We throw a resource not found exception here although it should be a Unauthorized.
                // The reason we do this is to prevent users form finding out which transaction was already
                // conducted and which was not.
            }

            if(entry.getType() == TransactionEntry.TransactionType.ORDER) {
                return order(entry, db);
            } else if(entry.getType() == TransactionEntry.TransactionType.PURCHASE) {
                return purchase(entry, db);
            }
            return mapSimpleTransaction(entry);

        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    private Order order(TransactionEntry entry, Database db) {
        OrderEntry orderEntry = db.orderGet(entry.getId());
        List<OrderedProductEntry> orderedProductEntries = db.orderGetProducts(orderEntry);
        OrderedProduct ordered[] = new OrderedProduct[orderedProductEntries.size()];
        for(int i = 0; i < ordered.length; i++) {
            ordered[i] = mapOrderedProduct(orderedProductEntries.get(i));
        }

        Order order = new Order(mapSimpleTransaction(orderEntry));
        order.setAdmin(orderEntry.getAdminId());
        order.setPurchased(ordered);
        return order;
    }

    public Purchase purchase(TransactionEntry entry, Database db) {
        Purchase purchase = new Purchase(mapSimpleTransaction(entry));
        List<ProductBoughtEntry> productCountEntries = db.purchaseGetProducts(entry);
        ProductCountPrice purchased[] = new ProductCountPrice[productCountEntries.size()];
        for(int i = 0; i < purchased.length; i++) {
            purchased[i] = mapProductCountPrice(productCountEntries.get(i));
        }
        purchase.setPurchased(purchased);
        return purchase;
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/v1/transactions/transfer")
    public ResponseEntity createTransfer(@RequestBody CreateTransfer transfer,
                                         @AuthenticationPrincipal UserPwdTocken userToken)
        throws Exception {
        if(userToken.getRole() == User.Role.ADMIN) {
            checkIfNotNull(transfer.getSender());
        } else {
            transfer.setSender(userToken.getId());
        }
        checkIfNotNull(transfer.getAmount());
        checkIfNotNull(transfer.getReceiver());

        Database db = Database.getInstance();
        TransactionEntry entry = new TransactionEntry();
        entry.setAmount(new BigDecimal(transfer.getAmount() / 100.00));
        entry.setSenderId(transfer.getSender());
        entry.setRecipientId(transfer.getReceiver());
        db.transactionTransfer(entry);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/v1/transactions/deposit")
    public ResponseEntity createDeposit(@RequestBody CreateDeposit deposit,
                                        @AuthenticationPrincipal UserPwdTocken userToken)
        throws Exception {
        checkIfNotNull(deposit.getAmount());

        Database db = Database.getInstance();
        TransactionEntry entry = new TransactionEntry();
        entry.setRecipientId(userToken.getId());
        entry.setAmount(new BigDecimal(deposit.getAmount() / 100.00));
        db.transactionDeposit(entry);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/v1/transactions/withdraw")
    public ResponseEntity createWithdraw(@RequestBody CreateWithdraw withdraw,
                                         @AuthenticationPrincipal UserPwdTocken userToken)
        throws Exception {
        checkIfNotNull(withdraw.getAmount());

        Database db = Database.getInstance();
        TransactionEntry entry = new TransactionEntry();
        entry.setSenderId(userToken.getId());
        entry.setAmount(new BigDecimal(withdraw.getAmount()/100.00));

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/v1/transactions/order")
    public ResponseEntity createOrder(@RequestBody CreateOrder order,
                                      @AuthenticationPrincipal UserPwdTocken userToken)
        throws Exception {
        checkIfNotNull(order.getAmount());
        checkIfNotNull(order.getOrders());

        Database db = Database.getInstance();
        OrderEntry entry = new OrderEntry();
        entry.setAdminId(userToken.getId());
        entry.setAmount(new BigDecimal(order.getAmount()/100.00));
        List<OrderedProductEntry> orderedProductEntries = new ArrayList<>(order.getOrders().length);
        for(OrderedProduct op : order.getOrders()) {
            checkIfBelowOne(op.getAmount());
            checkIfBelowOne(op.getProduct_info());

            OrderedProductEntry ope = new OrderedProductEntry();
            ope.setProductId(op.getProduct_info());
            ope.setCount(op.getAmount());
            orderedProductEntries.add(ope);
        }
        db.transactionOrder(entry, orderedProductEntries);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/v1/transactions/purchase")
    public ResponseEntity createPurchase(@RequestBody CreatePurchase purchase,
                                         @AuthenticationPrincipal UserPwdTocken userToken)
        throws Exception {
        checkIfNotNull(purchase.getOrders());

        Database db = Database.getInstance();
        TransactionEntry entry = new TransactionEntry();
        entry.setSenderId(userToken.getId());

        List<ProductBoughtEntry> productCountEntries = new ArrayList<>(purchase.getOrders().length);
        for(ProductAmount productAmount : purchase.getOrders()) {
            checkIfBelowOne(productAmount.getAmount());
            checkIfBelowOne(productAmount.getProduct());

            ProductBoughtEntry pbe = new ProductBoughtEntry();
            pbe.setProductId(productAmount.getProduct());
            pbe.setCount(productAmount.getAmount());
            productCountEntries.add(pbe);
        }

        db.transactionPurchase(entry, productCountEntries);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
