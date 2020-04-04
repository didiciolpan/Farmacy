package Domain;

import Repository.IRepositiory;
import Repository.InMemoryRepository;
import Service.DrugService;
import Service.TransactionService;
import Service.UndoRedoService;
import org.junit.Test;

import java.security.KeyException;

import static org.junit.Assert.*;

public class DrugTest {


    private IRepositiory<Drug> drugIRepository = null;
    private DrugService drugService = null;
    private TransactionService transactionService = null;
    private UndoRedoService undoRedoService = new UndoRedoService();

    public void setUp() throws KeyException {

        drugIRepository = new InMemoryRepository<>();
        drugService = new DrugService(drugIRepository, new DrugValidator(), undoRedoService);

        drugService.add("paracetamol", "terapia", 10, 5, false);
        drugService.add("nurofen", "reckitt", 20.5, 10, false);
        drugService.add("vitamina c", "bayer", 13.5, 10, false);
        drugService.add("zinat", "terapia", 9.5, 10, true);
    }

    @Test
    public void testConstructorSetsFieldsCorrectly() {

        Drug drug = new Drug("paracetamol", "terapia", 10, 5, false);

        assertEquals("paracetamol", drug.getDrugName());
        assertEquals("terapia", drug.getProducer());
        assertEquals(10, drug.getPrice(), 0);
        assertEquals(5, drug.getNoOfPieces());
        assertFalse(drug.isNeedsPrescription());
    }

    @Test
    public void testSetMethodsWorkCorrectly() {

        Drug drug = new Drug("paracetamol", "terapia", 10, 5, false);
        drug.setDrugName("Algocalmin");
        drug.setProducer("Bayer");
        drug.setPrice(20);
        drug.setNoOfPieces(10);
        drug.setNeedsPrescription(true);

        assertEquals("Algocalmin", drug.getDrugName());
        assertEquals("Bayer", drug.getProducer());
        assertEquals(20, drug.getPrice(), 0);
        assertEquals(10, drug.getNoOfPieces());
        assertTrue(drug.isNeedsPrescription());
    }

}