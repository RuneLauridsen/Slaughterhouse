

package slaughterhouse.shared;

import java.time.LocalDate;

public class Pig {
    private double weight;
    private int registrationNumber;
    private String farm;
    private LocalDate registrationDate;
    private LocalDate splitDate;

    public Pig() {
        // Tom constructor, så vi kan JSON deserialize.
    }

    public Pig(double weight, int registrationNumber, String farm, LocalDate registrationDate, LocalDate splitDate) {
        this.weight = weight;
        this.registrationNumber = registrationNumber;
        this.farm = farm;
        this.registrationDate = registrationDate;
        this.splitDate = splitDate;
    }

    public Pig(double weight, int registrationNumber, String farm) {
        this.weight = weight;
        this.registrationNumber = registrationNumber;
        this.farm = farm;
        this.registrationDate = LocalDate.now();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getSplitDate() {
        return splitDate;
    }

    public void setSplitDate(LocalDate splitDate) {
        this.splitDate = splitDate;
    }
}
