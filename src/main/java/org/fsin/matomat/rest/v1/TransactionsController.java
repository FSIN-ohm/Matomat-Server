package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.dao.TransactionDAO;
import org.fsin.matomat.database.model.OrderEntry;
import org.fsin.matomat.database.model.OrderedProductEntry;
import org.fsin.matomat.database.model.ProductCountEntry;
import org.fsin.matomat.database.model.TransactionEntry;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TransactionsController {

    private Transaction mapSimpleTransaction(TransactionEntry entry) {
        Transaction transaction = new Transaction();
        transaction.setDate(entry.getDate());
        transaction.setAmount(entry.getAmount().intValue());
        transaction.setId(entry.getId());
        transaction.setReceiver(entry.getRecipientId());
        transaction.setSender(entry.getSenderId());
        transaction.setTransaction_type(entry.getType().value());
        return transaction;
    }

    private ProductAmount mapProductAmount(ProductCountEntry entry) {
        ProductAmount amount = new ProductAmount();
        amount.setAmount(entry.getCount());
        amount.setProduct(entry.getProductsId());
        return amount;
    }

    private OrderedProduct mapOrderedProduct(OrderedProductEntry entry) {
        OrderedProduct product = new OrderedProduct();
        product.setAmount(entry.getCount());
        product.setProduct_info(entry.getInfo_id());
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
        Database db = Database.getInstance();
        TransactionEntry entry = db.transactionGet(id);
        if(entry.getType() == TransactionEntry.TransactionType.ORDER) {
            return order(entry, db);
        } else if(entry.getType() == TransactionEntry.TransactionType.PURCHASE) {
            return purchase(entry, db);
        }
        return mapSimpleTransaction(entry);
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
}
