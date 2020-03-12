package Domain;

public class TransactionValidator {

    public void validate(Transaction transaction) {
        if (transaction.getNoOfPiecesPerTransaction() <= 0) {
            throw new RuntimeException("The number of pieces for a transaction must be at least 1.");
        }
    }

}
