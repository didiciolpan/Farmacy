package Service;

import Domain.Drug;
import Domain.DrugValidator;
import Domain.Transaction;
import Domain.ValidationException;
import Repository.IRepositiory;
import Repository.InMemoryRepository;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyException;
import java.util.List;

import static org.junit.Assert.*;

public class DrugServiceTest {

    private IRepositiory<Drug> drugIRepository = null;
    private DrugService drugService = null;
    private UndoRedoService undoRedoService = null;

    @Before
    public void setUp() throws KeyException {

        DrugService drugService = getDrugService();
        drugService.add("paracetamol", "terapia", 10, 5, false);
        drugService.add("nurofen", "reckitt", 20.5, 10, false);
        drugService.add("vitamina c", "bayer", 13.5, 10, false);
        drugService.add("zinat", "terapia", 9.5, 10, true);
    }

    private DrugService getDrugService() {
        if (drugService == null) {
            drugService = new DrugService(getDrugRepository(), new DrugValidator(), getUndoRedoService());
        }
        return drugService;
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
    public void testDrugIsAdded() throws KeyException {

        drugIRepository = new InMemoryRepository<>();
        drugService = new DrugService(drugIRepository, new DrugValidator(), undoRedoService);

        Drug drugToBeAdded = new Drug("paracetamol", "terapia", 10, 5, false);
        drugService.add(drugToBeAdded.getDrugName(), drugToBeAdded.getProducer(), drugToBeAdded.getPrice(),
                drugToBeAdded.getNoOfPieces(), drugToBeAdded.isNeedsPrescription());

        assertEquals("paracetamol", drugToBeAdded.getDrugName());
    }

    @Test
    public void testIncreasePrice() throws KeyException {
        List<Drug> normalPrice = drugService.getAll();
        double percent = 50;
        double value = 15;
        List<Drug> increasedPrice = drugService.increasePriceOfDrugsByPercent(percent, value);
        assertNotEquals(normalPrice,increasedPrice);

    }

    @Test
    public void testValidationExceptionPrice() throws KeyException {
        boolean thrownException = false;
        drugIRepository = new InMemoryRepository<>();
        drugService = new DrugService(drugIRepository, new DrugValidator(), undoRedoService);

        Drug drugToBeAdded = new Drug("paracetamol", "terapia", 0, 5, false);
        try {
            drugService.add(drugToBeAdded.getDrugName(), drugToBeAdded.getProducer(), drugToBeAdded.getPrice(),
                    drugToBeAdded.getNoOfPieces(), drugToBeAdded.isNeedsPrescription());
        } catch (ValidationException e) {
            thrownException = true;
        }
        assertTrue(thrownException);
    }

    @Test
    public void testDrugIsUpdated() throws KeyException {

        int id = 1;

        Drug drugtoBeUpdated = drugService.searchById(id);

        String drugName = "zinat";
        String producer = "Terapia";

        double price;
        while (true) {
            price = 12.0;
            try {
                drugService.validatePrice(price);
                break;
            } catch (ValidationException ex) {
                System.out.println(ex.getMessage());
            }
        }
        int noOfPieces = 5;
        boolean needsPrescription = true;

        drugService.update(id, drugName, producer, price, noOfPieces, needsPrescription);

        assertEquals("zinat", drugtoBeUpdated.getDrugName());
        assertEquals("Terapia", drugtoBeUpdated.getProducer());
        assertEquals(12.0, drugtoBeUpdated.getPrice(), 0);
        assertEquals(5, drugtoBeUpdated.getNoOfPieces());
        assertFalse(drugtoBeUpdated.isNeedsPrescription());
    }

    @Test
    public void testUndoUpdate () throws KeyException {
        int id = 1;

        Drug actualDrug = drugService.searchById(id);
        Drug backUp =  new Drug(actualDrug.getDrugName(), actualDrug.getProducer(), actualDrug.getPrice(),
                actualDrug.getNoOfPieces(), actualDrug.isNeedsPrescription());
        backUp.setId(id);
        actualDrug.setDrugName("zinat");
        actualDrug.setProducer("Terapia");
        actualDrug.setPrice(12);
        actualDrug.setNoOfPieces(30);
        actualDrug.setNeedsPrescription(true);

        drugService.update(id,actualDrug.getDrugName(),actualDrug.getProducer(),actualDrug.getPrice(),actualDrug.getNoOfPieces(),actualDrug.isNeedsPrescription());
        undoRedoService.undo();
        assertNotEquals(backUp,actualDrug);
    }

    @Test
    public void testRedoUpdate () throws KeyException {
        int id = 1;

        Drug drugToBeUpdated = drugService.searchById(id);
        Drug backUp =  new Drug(drugToBeUpdated.getDrugName(), drugToBeUpdated.getProducer(), drugToBeUpdated.getPrice(),
               drugToBeUpdated.getNoOfPieces(), drugToBeUpdated.isNeedsPrescription());
        backUp.setId(id);

        drugService.update(id, drugToBeUpdated.getDrugName(), drugToBeUpdated.getProducer(), drugToBeUpdated.getPrice(),
                drugToBeUpdated.getNoOfPieces(), drugToBeUpdated.isNeedsPrescription());

        this.undoRedoService.undo();
        this.undoRedoService.redo();

        assertEquals(backUp,drugToBeUpdated);
    }

    @Test
    public void testUndo() throws KeyException {

        Drug drugToBeAdded = new Drug("zinat", "terapia", 15, 5, true);
        drugToBeAdded.setId(15);
        int addedId = drugToBeAdded.getId();
        drugService.add(drugToBeAdded.getDrugName(), drugToBeAdded.getProducer(), drugToBeAdded.getPrice(),
                drugToBeAdded.getNoOfPieces(), drugToBeAdded.isNeedsPrescription());

        this.undoRedoService.undo();
        assertNull(drugService.searchById(addedId));
    }

    @Test
    public void testRedo() throws KeyException {

        Drug drugToBeAdded = new Drug("zinat", "terapia", 15, 5, true);
        drugToBeAdded.setId(15);
        int addedId = drugToBeAdded.getId();
        drugService.add(drugToBeAdded.getDrugName(), drugToBeAdded.getProducer(), drugToBeAdded.getPrice(),
                drugToBeAdded.getNoOfPieces(), drugToBeAdded.isNeedsPrescription());

        this.undoRedoService.undo();
        this.undoRedoService.redo();
        assertEquals(15,addedId);
    }

    @Test
    public void testRemoveDrugException() {
        boolean thrownException = false;
        try {
            setUp();
            int drugId = 25;
            drugService.remove(drugId);
            System.out.println("Drug removed!");
        } catch (KeyException ex) {
            thrownException = true;
        }
        assertTrue(thrownException);
    }

    @Test
    public void testUndoRemove() throws KeyException {
            setUp();
            int drugId = 1;
         //   Drug drugToRemove = drugService.searchById(drugId);
            drugService.remove(drugId);
            this.undoRedoService.undo();
            assertNotNull(drugService.searchById(drugId));
    }

    @Test
    public void testRedoRemove() throws KeyException {
        setUp();
        int drugId = 1;
        //   Drug drugToRemove = drugService.searchById(drugId);
        drugService.remove(drugId);
        this.undoRedoService.undo();
        this.undoRedoService.redo();
        assertNull(drugService.searchById(drugId));
    }


    @Test
    public void testDrugSuccessfullyRemoved() {
        try {
            setUp();
            int drugId = 1;
            drugService.remove(drugId);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Errors:\n" + ex.getMessage());
        }
        assertNull(drugService.searchById(1));
    }

    @Test
    public void testDrugSearchByKey() {

        String drugToBeSearched = "paracetamol";
        List<Drug> foundDrug = drugService.searchDrugKey(drugToBeSearched);

        if (foundDrug.isEmpty()) {
            System.out.println("Drug not found");
        } else {
            for (Drug found : foundDrug) {
                System.out.println(found.toString());
//                System.out.println("\n" + "Search result: " + "\n"
//                        + "Drug{" + "\n"
//                        + "Drug name: " + found.getDrugName() + "\n"
//                        + "Drug producer: " + found.getProducer() + "\n"
//                        + "Drug price: " + found.getPrice() + " lei" + "\n"
//                        + "No. of pieces available: " + found.getNoOfPieces() + " pieces" + "\n"
//                        + "Drug needs prescription: " + found.isNeedsPrescription());
                System.out.println();
                assertEquals("paracetamol", found.getDrugName());
            }
        }
    }

    @Test( expected = KeyException.class)
    public void testKeyExceptionUndoRedo() throws KeyException {

            Drug drugToBeAdded = new Drug("zinat", "terapia", 15, 5, true);
            drugToBeAdded.setId(0);
            drugService.add(drugToBeAdded.getDrugName(), drugToBeAdded.getProducer(), drugToBeAdded.getPrice(),
                    drugToBeAdded.getNoOfPieces(), drugToBeAdded.isNeedsPrescription());
            drugService.removeForever(drugToBeAdded.getId());
            undoRedoService.undo();
            undoRedoService.undo();
            undoRedoService.undo();
            undoRedoService.undo();
            undoRedoService.undo();

    }


    @Test
    public void testSearchDrugById() {
        int id = 2;
        Drug searchedDrug = drugService.searchById(id);
        assertEquals(2, searchedDrug.getId());
    }

    @Test
    public void testGetAllDrug() {
        List<Drug> drugs = drugService.getAll();
        assertEquals(4, drugs.size());
    }
}
