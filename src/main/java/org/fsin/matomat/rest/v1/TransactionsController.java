package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.dao.TransactionDAO;
import org.fsin.matomat.database.model.TransactionEntry;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.Transaction;
import org.springframework.dao.EmptyResultDataAccessException;
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
}
