package Domain;

import java.util.Objects;

public class Drug extends Entity {

    private String drugName;
    private String producer;
    private double price;
    private int noOfPieces;
    private boolean needsPrescription;

    public Drug(String drugName, String producer, double price, int noOfPieces, boolean needsPrescription) {
        this.drugName = drugName;
        this.producer = producer;
        this.price = price;
        this.noOfPieces = noOfPieces;
        this.needsPrescription = needsPrescription;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "id = " + getId() +
                ", drugName='" + drugName + '\'' +
                ", producer='" + producer + '\'' +
                ", price=" + price +
                ", noOfPieces=" + noOfPieces +
                ", needsPrescription=" + needsPrescription +
                '}';
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drug drug = (Drug) o;
        return drug.getId() == ((Drug) o).getId() &&
                Double.compare(drug.getPrice(), getPrice()) == 0 &&
                getNoOfPieces() == drug.getNoOfPieces() &&
                isNeedsPrescription() == drug.isNeedsPrescription() &&
                Objects.equals(getDrugName(), drug.getDrugName()) &&
                Objects.equals(getProducer(), drug.getProducer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),getDrugName(), getProducer(), getPrice(), getNoOfPieces(), isNeedsPrescription());
    }
}
