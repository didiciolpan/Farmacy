package Domain;

import Repository.IRepositiory;
import Repository.InMemoryRepository;
import Service.DrugService;
import Service.TransactionService;
import Service.UndoRedoService;
import org.junit.Test;

import java.security.KeyException;

import static org.junit.Assert.assertEquals;

public class TransactionTest {


    private IRepositiory<Drug> drugIRepository = null;
    private IRepositiory<Transaction> transactionIRepositiory = null;
    private DrugService drugService = null;
    private TransactionService transactionService = null;
    private UndoRedoService undoRedoService = new UndoRedoService();

    public void setUp() throws KeyException {

        transactionIRepositiory = new InMemoryRepository<>();
        transactionService = new TransactionService(transactionIRepositiory, drugIRepository,
                new TransactionValidator(), undoRedoService);
        drugIRepository = new InMemoryRepository<>();
        drugService = new DrugService(drugIRepository, new DrugValidator(), undoRedoService);

        drugService.add("paracetamol", "terapia", 10, 5, false);
        drugService.add("nurofen", "reckitt", 20.5, 10, false);
        drugService.add("vitamina c", "bayer", 13.5, 10, false);
        drugService.add("zinat", "terapia", 9.5, 10, true);

        transactionService.add(1, 13434, 5, "12.01.2020", "10:10");
        transactionService.add(2, 2445, 1, "20.02.2020", "14:27");
        transactionService.add(2, 2445, 1, "10.03.2020", "18:23");
        transactionService.add(3, 963426, 2, "20.05.2020", "17:13");

    }

    @Test
    public void testConstructorSetsFieldsCorrectly() {

        Transaction transaction = new Transaction(1, 13434, 5,
                "12.01.2020", "10:10");

        assertEquals(1, transaction.getTransactionDrugId());
        assertEquals(13434, transaction.getCard());
        assertEquals(5, transaction.getNoOfPiecesPerTransaction());
        assertEquals("12.01.2020", transaction.getDate());
        assertEquals("10:10", transaction.getTime());
    }

    @Test
    public void testSetMethodsWorkCorrectly() {

        Transaction transaction = new Transaction(1, 324235, 10, "12.01.2020", "10:10");
        transaction.setTransactionDrugId(1);
        transaction.setCard(324235);
        transaction.setNoOfPiecesPerTransaction(10);
        transaction.setDate("12.01.2020");
        transaction.setTime("10:10");

        assertEquals(1, transaction.getTransactionDrugId());
        assertEquals(324235, transaction.getCard());
        assertEquals(10, transaction.getNoOfPiecesPerTransaction());
        assertEquals("12.01.2020", transaction.getDate());
        assertEquals("10:10", transaction.getTime());
    }


}
