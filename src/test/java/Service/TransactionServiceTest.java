package Service;

import Domain.*;
import Repository.IRepositiory;
import Repository.InMemoryRepository;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TransactionServiceTest {
    // todo: test coverage 90-100%
    // todo: folosirea metodei setup

    private IRepositiory<Drug> drugIRepository = null;
    private IRepositiory<Transaction> transactionIRepository = null;
    private DrugService drugService = null;
    private TransactionService transactionService = null;
    private UndoRedoService undoRedoService = null;

    @Before
    public void setUp() throws KeyException {

        DrugService drugService = getDrugService();
        drugService.add("paracetamol", "terapia", 10, 5, false);
        drugService.add("nurofen", "reckitt", 20.5, 10, false);
        drugService.add("vitamina c", "bayer", 13.5, 10, false);
        drugService.add("zinat", "terapia", 9.5, 10, true);

        TransactionService transactionService = getTransactionService();
        transactionService.add(1, 13434, 5, "12.01.2020", "10:10");
        transactionService.add(2, 2445, 1, "20.02.2020", "14:27");
        transactionService.add(2, 2445, 1, "10.03.2020", "18:23");
        transactionService.add(3, 963426, 2, "20.05.2020", "17:13");
    }

    private TransactionService getTransactionService() {
        if (transactionService == null) {
            transactionService = new TransactionService(getTransactionRepository(), getDrugRepository(), new TransactionValidator(), getUndoRedoService());
        }
        return transactionService;
    }

    private DrugService getDrugService() {
        if (drugService == null) {
            drugService = new DrugService(getDrugRepository(), new DrugValidator(), getUndoRedoService());
        }
        return drugService;
    }

    private IRepositiory<Transaction> getTransactionRepository() {
        if (transactionIRepository == null) {
            transactionIRepository = new InMemoryRepository<>();
        }
        return transactionIRepository;
    }

    private IRepositiory<Drug> getDrugRepository() {
        if (drugIRepository == null) {
            drugIRepository = new InMemoryRepository<>();
        }
        return drugIRepository;
    }

    private UndoRedoService getUndoRedoService() {
        if (undoRedoService == null) {
            undoRedoService = new UndoRedoService();
        }
        return undoRedoService;
    }

    @Test
    public void testGetAllTransactionBetweenDates(){
        TransactionService tranService = getTransactionService();

        List<Transaction> transactions = tranService.getAllTransactionBetweenDates("12.02.2020", "10.03.2020");
        assertEquals(2, transactions.size());

        transactions = tranService.getAllTransactionBetweenDates("01.03.2020", "10.03.2020");
        assertEquals(1, transactions.size());
        assertEquals("10.03.2020", transactions.get(0).getDate());
    }

    @Test
    public void testGetNoOfTransactionForDrug(){
        TransactionService tranService = getTransactionService();
        Map<Drug, Integer> sortedMap = tranService.getDrugsOrderedDescByTransactionNumber();
        //Am testat ca nr de drugs din repository e acelasi cu numar de drugs din map-ul sortat
        assertEquals(getDrugRepository().getAll().size(), sortedMap.size());
    }


    @Test
    public void testTransactionIsAdded() throws KeyException {

        TransactionService transactionService = getTransactionService();
        Transaction transactionToBeAdded = new Transaction(1, 3214235, 2, "28.03.2020", "09:11");
        transactionToBeAdded.setId(0);
        transactionService.add(transactionToBeAdded.getTransactionDrugId(), transactionToBeAdded.getCard(),
                transactionToBeAdded.getNoOfPiecesPerTransaction(), transactionToBeAdded.getDate(), transactionToBeAdded.getTime());
        assertEquals(0, transactionToBeAdded.getId());
    }

    @Test
    public void testTransactionIsUpdated(){

        int id = 2;
        Transaction transactionToBeUpdated = transactionService.searchById(id);

        int transactionDrugId = 3;
        int card = 315745;

        int noOfPiecesPerTransaction;
        while (true) {
            noOfPiecesPerTransaction = 7;
            try {
                transactionService.validateQuantiry(noOfPiecesPerTransaction);
                break;
            } catch (ValidationException ex) {
                System.out.println(ex.getMessage());
            }
        }

        String date = "12.01.2020";
        String time = "14:45";
        try {
            transactionService.update(id, transactionDrugId, card, noOfPiecesPerTransaction, date, time);
        } catch (KeyException kex) {
            System.out.println("Exceptie ID: " + kex.getMessage());
        }

        assertEquals(3, transactionToBeUpdated.getTransactionDrugId());
        assertEquals(315745, transactionToBeUpdated.getCard());
        assertEquals(7, transactionToBeUpdated.getNoOfPiecesPerTransaction());
        assertEquals("12.01.2020", transactionToBeUpdated.getDate());
        assertEquals("14:45", transactionToBeUpdated.getTime());
    }

    @Test
    public void testRemoveTransactionException() {
        boolean thrownException = false;
        try {
            int transactionId = 25;
            transactionService.removeTransaction(transactionId);
        } catch (KeyException ex) {
            thrownException = true;
        }
        assertTrue(thrownException);
    }

    @Test
    public void testTransactionSuccessfullyRemoved() {
        try {
            int transactionId = 1;
            transactionService.removeTransaction(transactionId);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Errors:\n" + ex.getMessage());
        }
        assertNull(transactionService.searchById(1));
    }

    @Test
    public void testDrugWithTransactionSuccessfullyRemoved() throws KeyException {
        int drugId = 0;
        int count = 0;

        List<Transaction> transactions = transactionService.getAll();
        transactionService.removeDrugAndTransaction(drugId);
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionDrugId() == drugId) {
                count++;
            }
        }
        System.out.println("Drug removed!");

        assertEquals(0, count);
        assertNull(drugService.searchById(drugId));
    }

    @Test
    public void testUndoDeleteDrugWithTransaction() throws KeyException {
        int drugId = 2;
        List<Transaction> transactionBackup = new ArrayList<>();

        List<Transaction> transactions = transactionService.getAll();
        transactionService.removeDrugAndTransaction(drugId);
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionDrugId() == drugId) {
                transactionBackup.add(transaction);
            }
        }
        System.out.println("Drug removed!");
        this.undoRedoService.undo();
        assertNotNull(drugService.searchById(drugId));
        assertEquals(2,transactionBackup.size());
    }

    @Test
    public void testRedoDeleteDrugWithTransaction() throws KeyException {
        int drugId = 2;
        List<Transaction> transactionBackup = new ArrayList<>();

        List<Transaction> transactions = transactionService.getAll();
        transactionService.removeDrugAndTransaction(drugId);
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionDrugId() != drugId) {
                transactionBackup.add(transaction);
            }
        }
        System.out.println("Drug removed!");
        this.undoRedoService.undo();
        this.undoRedoService.redo();
        assertNull(drugService.searchById(drugId));
        assertEquals(2,transactionBackup.size());
    }

    @Test
    public void testTransactionSearchByKey(){

        String transactionSearch = "12.01.2020";
        List<Transaction> foundTransaction = transactionService.searchTransactionKey(transactionSearch);
        if (foundTransaction.isEmpty()) {
            System.out.println("Transaction not found");
        } else {
            for (Transaction found : foundTransaction) {
                System.out.println(found.toString());
                assertEquals("12.01.2020", found.getDate());
            }
        }
    }

    @Test
    public void testClientCardOrderedDescByTransactionNumber() {
        TransactionService tranService = getTransactionService();

        Map<Transaction, Integer> sortedMap = tranService.getClientCardOrderedDescByTransactionNumber();
        //Am testat ca nr de transactii din repository e acelasi cu numar de transactii din map-ul sortat
        assertEquals(getTransactionRepository().getAll().size(), sortedMap.size());
    }

    @Test
    public void testRuntimeExceptionQuantity() {
        boolean thrownException = false;
        try {
            TransactionService transactionService = getTransactionService();
            Transaction transactionToBeAdded = new Transaction(1, 3214235, 0, "28.03.2020", "09:11");
            transactionService.add(transactionToBeAdded.getTransactionDrugId(), transactionToBeAdded.getCard(),
                    transactionToBeAdded.getNoOfPiecesPerTransaction(), transactionToBeAdded.getDate(), transactionToBeAdded.getTime());
        } catch (ValidationException re){
            thrownException = true;
        } catch (KeyException kex){
            System.out.println(kex.getMessage());
        }
        assertTrue(thrownException);
    }
}
