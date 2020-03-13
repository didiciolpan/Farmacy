package Service;

import Domain.Drug;
import Domain.Transaction;
import Repository.DrugRepository;
import Repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private TransactionRepository transactionRepository;
    private DrugRepository drugRepository;

    public TransactionService(TransactionRepository transactionRepository, DrugRepository drugRepository) {
        this.transactionRepository = transactionRepository;
        this.drugRepository = drugRepository;
    }

    public Transaction addOrUpdate(String transactionId, String transactionDrugId, int card, int noOfPiecesPerTransaction, String date, String time) {
        Transaction existing = transactionRepository.findById(transactionId);
        if (existing != null) {
            // keep unchanged fields as they were
            if (transactionDrugId.isEmpty()) {
                transactionDrugId = existing.getTransactionDrugId();
            }
            if (card == 0) {
                card = existing.getCard();
            }
            if (noOfPiecesPerTransaction == 0) {
                noOfPiecesPerTransaction = existing.getNoOfPiecesPerTransaction();
            }
            if (date.isEmpty()) {
                date = existing.getDate();
            }
            if (time.isEmpty()) {
                time = existing.getTime();
            }
        }

        Drug drugSold = drugRepository.findById(transactionDrugId);
        if (drugSold == null) {
            throw new RuntimeException("There is no drug with the given id!");
        }

        Transaction transaction = new Transaction(transactionId, transactionDrugId, card, noOfPiecesPerTransaction, date, time);
        transactionRepository.upsert(transaction);
        return transaction;
    }

    public void remove(String id) {
        transactionRepository.remove(id);
    }

    public List<Transaction> getAll() {
        return transactionRepository.getAll();
    }

}
