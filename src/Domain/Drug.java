package Domain;

public class Drug {

    private String drugId;
    private String drugName;
    private String producer;
    private double price;
    private int noOfPieces;
    private boolean needsPrescription;

    public Drug(String drugId, String drugName, String producer, double price, int noOfPieces, boolean needsPrescription) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.producer = producer;
        this.price = price;
        this.noOfPieces = noOfPieces;
        this.needsPrescription = needsPrescription;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "drugId='" + drugId + '\'' +
                ", drugName='" + drugName + '\'' +
                ", producer='" + producer + '\'' +
                ", price=" + price +
                ", noOfPieces=" + noOfPieces +
                ", needsPrescription=" + needsPrescription +
                '}';
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
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
