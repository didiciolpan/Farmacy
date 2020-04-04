package Service;

import Domain.*;
import Repository.IRepositiory;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;

public class DrugService {

    private IRepositiory<Drug> drugIRepository;
    private DrugValidator drugValidator;
    private UndoRedoService undoRedoService;

    public DrugService(IRepositiory<Drug> drugIRepositiory, DrugValidator drugValidator,
                       UndoRedoService undoRedoService) {
        this.drugIRepository = drugIRepositiory;
        this.drugValidator = drugValidator;
        this.undoRedoService = undoRedoService;

    }

    public void add(String drugName, String producer, double price, int noOfPieces, boolean needsPrescription) throws KeyException {
        Drug drug = new Drug(drugName, producer, price, noOfPieces, needsPrescription);
        drugValidator.validatePrice(drug.getPrice());
        this.drugIRepository.create(drug);
        this.undoRedoService.addToUndo(new AddOperation<Drug>(drugIRepository, drug));
    }

    public void update(int id, String drugName, String producer, double price, int noOfPieces, boolean needsPrescription) throws KeyException {
        Drug existing = drugIRepository.findById(id);
        // Drug backuop = existing;

        Drug backup = new Drug(existing.getDrugName(), existing.getProducer(), existing.getPrice(),
                existing.getNoOfPieces(), existing.isNeedsPrescription());
        backup.setId(id);

        if (!drugName.isEmpty()) {
            existing.setDrugName(drugName);
        }
        if (!producer.isEmpty()) {
            existing.setProducer(producer);
        }
        if (price != 0) {
            existing.setPrice(price);
        }
        if ((noOfPieces != 0)) {
            existing.setNoOfPieces(noOfPieces);
        }
        drugIRepository.update(existing);
        this.undoRedoService.addToUndo(new UpdateOperation<>(drugIRepository, backup, existing));

    }

    @SuppressWarnings("unchecked")
    public void remove(int drugId) throws KeyException {
        Drug drugToBeRemoved = drugIRepository.findById(drugId);
        drugIRepository.remove(drugId);
        this.undoRedoService.addToUndo(new DeleteOperation<Drug>(drugIRepository, drugToBeRemoved));
        //todo: test pe exceptie daca nu exista cand vreau sa sterg un Drug - done
        //todo: test ca se sterge cu succes - done
        //todo: cand sterg un medicament sa se stearga si tranzactiile acestuia si la undo sa
        //todo: vina inapoi si tranzactiile ->  metoda removeDrugAndTransaction in Transaction service
    }

    public void removeForever(int drugId) throws  KeyException{
        Drug drugToBeRemoved = drugIRepository.findById(drugId);
        drugIRepository.remove(drugId);
    }

    public Drug searchById(int drugId) {
        return drugIRepository.findById(drugId);
    }

    public List<Drug> getAll() {
        return drugIRepository.getAll();
    }

    public void validatePrice(double price) {
        drugValidator.validatePrice(price);
    }

    public List<Drug> searchDrugKey(String search) {
        List<Drug> drugsFound = new ArrayList<Drug>();   // lista goala unde stochez rezultatele
        List<Drug> drugs = getAll();
        for (Drug drug : drugs) {
            if (drug.getDrugName().equalsIgnoreCase(search) || drug.getProducer().equalsIgnoreCase(search)) {
                drugsFound.add(drug);
            }
        }
        return drugsFound;
    }

    public List<Drug> increasePriceOfDrugsByPercent(double percent, double value) {
        List<Drug> drugs = getAll();
        List<Drug> expensedDrugs = new ArrayList<Drug>();
        for (Drug drug : drugs) {
            if (drug.getPrice() < value) {
                double actualPrice = drug.getPrice();
                double increasedPrice = actualPrice * (percent / 100);
                drug.setPrice(increasedPrice);
                expensedDrugs.add(drug);
            }
        }
        return expensedDrugs;
    }
}
