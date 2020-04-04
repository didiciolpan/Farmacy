package Service;

import Domain.*;
import Repository.IRepositiory;

import java.security.KeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionService {

    private IRepositiory<Transaction> transactionIRepository;
    private IRepositiory<Drug> drugIRepositiory;
    private TransactionValidator transactionValidator;
    private UndoRedoService undoRedoService;

    public TransactionService(IRepositiory<Transaction> transactionIRepository,
                              IRepositiory<Drug> drugIRepository,
                              TransactionValidator transactionValidator,
                              UndoRedoService undoRedoService) {
        this.transactionIRepository = transactionIRepository;
        this.drugIRepositiory = drugIRepository;
        this.transactionValidator = transactionValidator;
        this.undoRedoService = undoRedoService;
    }

    @SuppressWarnings("unchecked")
    public void add(int transactionDrugId, int card, int noOfPiecesPerTransaction, String date, String time) throws KeyException {
        Drug drug = this.drugIRepositiory.read(transactionDrugId);
        if (drug == null) {
            throw new KeyException("The given drug id does not exist!");
        }
        if (drug.getNoOfPieces() <= 0) {
            throw new IllegalArgumentException("The number of pieces for a transaction must be at least 1.");
        }
        validateQuantiry(noOfPiecesPerTransaction);

        Transaction transaction = new Transaction(transactionDrugId, card, noOfPiecesPerTransaction, date, time);
        this.transactionIRepository.create(transaction);
        undoRedoService.addToUndo(new AddOperation<>(transactionIRepository, transaction));
    }

    public Transaction searchById(int transactionId) {
        return transactionIRepository.findById(transactionId);
    }

    public void update(int id, int transactionDrugId, int card, int noOfPiecesPerTransaction, String date, String time) throws KeyException {
        Transaction existing = transactionIRepository.findById(id);

        if (existing != null) {
            if (transactionDrugId != 0) {
                existing.setTransactionDrugId(transactionDrugId);

            }
            if (card != 0) {
                existing.setCard(card);
            }
            if (noOfPiecesPerTransaction != 0) {
                existing.setNoOfPiecesPerTransaction(noOfPiecesPerTransaction);
            }
            if (!date.isEmpty()) {
                existing.setDate(date);
            }
            if (!time.isEmpty()) {
                existing.setTime(time);
            }
        }
        transactionIRepository.update(existing);
    }

    public void removeTransaction(int id) throws KeyException {
        Transaction transactionToBeRemoved = transactionIRepository.findById(id);
        transactionIRepository.remove(id);
        this.undoRedoService.addToUndo(new DeleteOperation<Transaction>(transactionIRepository, transactionToBeRemoved));


    }

    @SuppressWarnings("unchecked")
    public void removeDrugAndTransaction(int drugId) throws KeyException {
        Drug drugToBeRemoved = drugIRepositiory.findById(drugId);
        List<Transaction> transToBeDeleted;
        transToBeDeleted = new ArrayList<>();
        List<Transaction> transactions = transactionIRepository.getAll();

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionDrugId() == drugId) {
                transToBeDeleted.add(transaction);
                transactionIRepository.remove(transaction.getId());
            }
        }
        drugIRepositiory.remove(drugId);
        this.undoRedoService.addToUndo(new DeleteOperationDrugAndTransactions<>(drugIRepositiory, drugToBeRemoved,
                transactionIRepository, transToBeDeleted));
    }

    public List<Transaction> getAll() {
        return transactionIRepository.getAll();
    }

    public void validateQuantiry(int noOItems) {
        transactionValidator.validateQuantity(noOItems);
    }

    public List<Transaction> searchTransactionKey(String search) {
        List<Transaction> transactionsFound = new ArrayList<>();   // lista goala unde stochez rezultatele
        List<Transaction> transactions = getAll();
        for (Transaction transaction : transactions) {
            if (transaction.getDate().equalsIgnoreCase(search) || transaction.getTime().equalsIgnoreCase(search)) {
                transactionsFound.add(transaction);
            }
        }
        return transactionsFound;
    }

    public List<Transaction> getAllTransactionBetweenDates(String dateStr1, String dateStr2) {
        List<Transaction> transByDate = new ArrayList<>();
        List<Transaction> transactions = transactionIRepository.getAll();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date1 = format.parse(dateStr1);
            Date date2 = format.parse(dateStr2);
            for (Transaction transaction : transactions) {
                Date transDate = format.parse(transaction.getDate());
                if (transDate.compareTo(date1) >= 0 && transDate.compareTo(date2) <= 0) {
                    transByDate.add(transaction);
                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return transByDate;
        }
        return transByDate;
    }

    public Map<Drug, Integer> getDrugsOrderedDescByTransactionNumber() {
        Map<Drug, Integer> drugNo = new LinkedHashMap<>();
        List<Drug> drugs = drugIRepositiory.getAll();
        List<Transaction> transactions = transactionIRepository.getAll();

        for (Drug drug : drugs) {
            drugNo.put(drug, getNoOfTransactionForDrug(drug.getId(), transactions));
        }

        List<Map.Entry<Drug, Integer>> sorted = new ArrayList<>(drugNo.entrySet());
        Collections.sort(sorted, new Comparator<Map.Entry<Drug, Integer>>() {
            @Override
            public int compare(Map.Entry<Drug, Integer> arg0, Map.Entry<Drug, Integer> arg1) {
                return arg1.getValue().compareTo(arg0.getValue());
            }
        });
        drugNo.clear();
        for (Map.Entry<Drug, Integer> value : sorted) {
            drugNo.put(value.getKey(), value.getValue());
        }
        return drugNo;
    }

    private Integer getNoOfTransactionForDrug(int drugId, List<Transaction> transactions) {
        Integer noOfTransaction = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionDrugId() == drugId) {
                noOfTransaction++;
            }
        }
        return noOfTransaction;
    }

    private Integer getNoOfTransactionForClientCard(int clientCard, List<Transaction> transactions) {
        Integer noOfTransaction = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getCard() == clientCard) {
                noOfTransaction++;
            }
        }
        return noOfTransaction;
    }

    public Map<Transaction, Integer> getClientCardOrderedDescByTransactionNumber() {
        Map<Transaction, Integer> trasnsactionsNo = new LinkedHashMap<>();
        List<Transaction> transactions = transactionIRepository.getAll();

        for (Transaction transaction : transactions) {
            trasnsactionsNo.put(transaction, getNoOfTransactionForClientCard(transaction.getCard(), transactions));
        }
        List<Map.Entry<Transaction, Integer>> sorted = new ArrayList<>(trasnsactionsNo.entrySet());
        Collections.sort(sorted, new Comparator<Map.Entry<Transaction, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Transaction, Integer> arg0, Map.Entry<Transaction, Integer> arg1) {
                        return arg1.getValue().compareTo(arg0.getValue());
                    }
                }
        );
        trasnsactionsNo.clear();
        for (Map.Entry<Transaction, Integer> value : sorted) {
            trasnsactionsNo.put(value.getKey(), value.getValue());
        }
        return trasnsactionsNo;
    }


}
