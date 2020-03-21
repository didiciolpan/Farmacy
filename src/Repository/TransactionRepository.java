package Repository;

import Domain.Transaction;
import Domain.TransactionValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {

    private Map<String, Transaction> storage = new HashMap<>();
    private TransactionValidator validator;

    public TransactionRepository(TransactionValidator validator) {
        this.validator = validator;
    }

    public Transaction findById(String transactionId) {
        return storage.get(transactionId);
    }

    public void update(Transaction transaction) {
        validator.validate(transaction);
        storage.put(transaction.getTransactionId(), transaction);
    }

    public void remove(String transactionId) {
        if (!storage.containsKey(transactionId)) {
            throw new RuntimeException("There is no transaction with the given transactionId to remove.");
        }

        storage.remove(transactionId);
    }

    public List<Transaction> getAll() {
        return new ArrayList<>(storage.values());
    }

}
