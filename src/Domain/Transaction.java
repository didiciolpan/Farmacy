package Domain;

public class Transaction {

    private String transactionId;
    private String transactionDrugId;
    private int card;
    private int noOfPiecesPerTransaction;
    private String date;
    private String time;

    public Transaction(String transactionId, String transactionDrugId, int card, int noOfPiecesPerTransaction, String date, String time) {
        this.transactionId = transactionId;
        this.transactionDrugId = transactionDrugId;
        this.card = card;
        this.noOfPiecesPerTransaction = noOfPiecesPerTransaction;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionDrugId=" + transactionDrugId +
                ", card=" + card +
                ", noOfPiecesPerTransaction=" + noOfPiecesPerTransaction +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDrugId() {
        return transactionDrugId;
    }

    public void setTransactionDrugId(String transactionDrugId) {
        this.transactionDrugId = transactionDrugId;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getNoOfPiecesPerTransaction() {
        return noOfPiecesPerTransaction;
    }

    public void setNoOfPiecesPerTransaction(int noOfPiecesPerTransaction) {
        this.noOfPiecesPerTransaction = noOfPiecesPerTransaction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
