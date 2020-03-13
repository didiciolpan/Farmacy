package Repository;

import Domain.Drug;
import Domain.DrugValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrugRepository {

    private Map<String, Drug> storage = new HashMap<>();
    private DrugValidator validator;

    public DrugRepository(DrugValidator validator) {
        this.validator = validator;
    }

    public Drug findById(String drugId) {
        return storage.get(drugId);
    }

    public void upsert(Drug drug) {
        validator.validate(drug);
        storage.put(drug.getDrugId(), drug);
    }

    public void remove(String drugId) {
        if (!storage.containsKey(drugId)) {
            throw new RuntimeException("There is no drug with the given id to remove.");
        }

        storage.remove(drugId);
    }

    public List<Drug> getAll() {
        return new ArrayList<>(storage.values());
    }
}
