package Domain;

public class DrugValidator {

    public void validate(Drug drug) {

        if (drug.getPrice() <= 0) {
            throw new RuntimeException("Price must be > 0.");
        }
    }

}
