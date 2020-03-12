package Domain;

import java.sql.Timestamp;
import java.util.Date;

public class Transaction {

  //  id, id_medicament (trebuie să existe), nr. card client (întreg), nr_bucăți, data și ora.
    private int transactionId;
    private int transactionDrugId;
    private int card;
    private int noOfPiecesPerTransaction;
    private String date;
    private String time;

    public Transaction(int transactionId, int transactionDrugId, int card, int noOfPiecesPerTransaction, String date, String time) {
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

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionDrugId() {
        return transactionDrugId;
    }

    public void setTransactionDrugId(int transactionDrugId) {
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
