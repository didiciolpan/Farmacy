package Domain;

public class Drug {

    private int drugId;
    private String producer;
    private double price;
    private int noOfPieces;
    private boolean needsPrescription;

    public Drug(int drugId, String producer, double price, int noOfPieces, boolean needsPrescription) {
        this.drugId = drugId;
        this.producer = producer;
        this.price = price;
        this.noOfPieces = noOfPieces;
        this.needsPrescription = needsPrescription;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "drugId=" + drugId +
                ", producer='" + producer + '\'' +
                ", price=" + price +
                ", noOfPieces=" + noOfPieces +
                ", needsPrescription=" + needsPrescription +
                '}';
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNoOfPieces() {
        return noOfPieces;
    }

    public void setNoOfPieces(int noOfPieces) {
        this.noOfPieces = noOfPieces;
    }

    public boolean isNeedsPrescription() {
        return needsPrescription;
    }

    public void setNeedsPrescription(boolean needsPrescription) {
        this.needsPrescription = needsPrescription;
    }
}
