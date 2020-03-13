package Service;

import Domain.Drug;
import Repository.DrugRepository;

import java.util.List;

public class DrugService {

    private DrugRepository drugRepository;

    public DrugService(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    public void addOrUpdate(String drugId, String drugName, String producer, double price, int noOFPieces, boolean needsPrescription) {
        Drug existing = drugRepository.findById(drugId);
        if (existing != null) {
            // keep unchanged fields as they were
            if (drugName.isEmpty()) {
                drugName = existing.getDrugName();
            }
            if (producer.isEmpty()) {
                producer = existing.getProducer();
            }
            if (price == 0) {
                price = existing.getPrice();
            }
            if (noOFPieces == 0) {
                noOFPieces = existing.getNoOfPieces();
            }
        }
        Drug drug = new Drug(drugId, drugName, producer, price, noOFPieces, needsPrescription);
        drugRepository.upsert(drug);
    }

    public void remove(String drugId) {
        drugRepository.remove(drugId);
    }

    public List<Drug> getAll() {
        return drugRepository.getAll();
    }

}
