package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.dao.TransactionDAO;
import org.fsin.matomat.database.model.*;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private ProductAmount mapProductAmount(ProductCountEntry entry) {
        ProductAmount amount = new ProductAmount();
        amount.setAmount(entry.getCount());
        amount.setPrice(entry.getPriceId());
        return amount;
    }

    private OrderedProduct mapOrderedProduct(OrderedProductEntry entry) {
        OrderedProduct product = new OrderedProduct();
        product.setAmount(entry.getCount());
        product.setProduct_info(entry.getProductId());
        return product;
    }

    @RequestMapping("/v1/transactions")
    public Transaction[] transactions(@RequestParam(value="count", defaultValue="-1") int count,
                                      @RequestParam(value="page", defaultValue="0") int page,
                                      @RequestParam(value="user", defaultValue="-1") int user,
                                      @RequestParam(value="type", defaultValue="") String typeRaw)
        throws Exception {
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
            Transaction transactions[] = new Transaction[transactionEntries.size()];
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

    @RequestMapping("/v1/transactions/{id}")
    public Transaction transaction(@PathVariable long id)
        throws Exception {
        try {
            Database db = Database.getInstance();
            TransactionEntry entry = db.transactionGet(id);
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
        List<ProductCountEntry> productCountEntries = db.purchaseGetProducts(entry);
        ProductAmount purchased[] = new ProductAmount[productCountEntries.size()];
        for(int i = 0; i < purchased.length; i++) {
            purchased[i] = mapProductAmount(productCountEntries.get(i));
        }
        purchase.setProducts(purchased);
        return purchase;
    }

    @PostMapping("/v1/transactions/transfer")
    public ResponseEntity createTransfer(@RequestBody CreateTransfer transfer)
        throws Exception {
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

    @PostMapping("/v1/transactions/deposit")
    public ResponseEntity createDeposit(@RequestBody CreateDeposit deposit)
        throws Exception {
        checkIfNotNull(deposit.getAmount());

        Database db = Database.getInstance();
        TransactionEntry entry = new TransactionEntry();
        entry.setRecipientId(3);    //TODO: Make this work with the current loged in user
        entry.setAmount(new BigDecimal(deposit.getAmount() / 100.00));
        db.transactionDeposit(entry);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/v1/transactions/withdraw")
    public ResponseEntity createWithdraw(@RequestBody CreateWithdraw withdraw)
        throws Exception {
        checkIfNotNull(withdraw.getAmount());

        Database db = Database.getInstance();
        TransactionEntry entry = new TransactionEntry();
        entry.setSenderId(3);  //TODO: Make this work with the current loged in user
        entry.setAmount(new BigDecimal(withdraw.getAmount()/100.00));

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/v1/transactions/order")
    public ResponseEntity createOrder(@RequestBody CreateOrder order)
        throws Exception {
        checkIfNotNull(order.getAmount());
        checkIfNotNull(order.getOrders());

        Database db = Database.getInstance();
        OrderEntry entry = new OrderEntry();
        entry.setAdminId(3);        //TODO: Make this work with the current loged in admin
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

    @PostMapping("/v1/transactions/purchase")
    public ResponseEntity createPurchase(@RequestBody CreatePurchase purchase)
        throws Exception {
        checkIfNotNull(purchase.getOrders());

        Database db = Database.getInstance();
        TransactionEntry entry = new TransactionEntry();
        entry.setSenderId(3); //TODO: Make this work with the current loged in user

        List<ProductCountEntry> productCountEntries = new ArrayList<>(purchase.getOrders().length);
        for(ProductAmount productAmount : purchase.getOrders()) {
            checkIfBelowOne(productAmount.getAmount());
            checkIfBelowOne(productAmount.getPrice());

            ProductCountEntry pe = new ProductCountEntry();
            pe.setPriceId(productAmount.getPrice());
            pe.setCount(productAmount.getAmount());
            productCountEntries.add(pe);
        }

        db.transactionPurchase(entry, productCountEntries);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
