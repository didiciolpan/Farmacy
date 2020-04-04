package Domain;

public class TransactionValidator {

    public void validateQuantity(int noOfItems) {
        if (noOfItems <= 0) {
            throw new ValidationException("The number of pieces for a transaction must be at least 1.");
        }
    }
}
