package Domain;

public class DrugValidator {

    public void validatePrice(double price) {

        if (price <= 0) {
            throw new ValidationException("Price must be > 0.");
        }
    }

}
