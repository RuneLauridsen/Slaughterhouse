package slaughterhouse.shared;

import java.util.List;

public class Tray {
    private int trayId;
    private List<PigPart> pigParts;
    private int maxWeight;

    public Tray(int trayId, List<PigPart> pigParts, int maxWeight) {
        this.trayId = trayId;
        this.pigParts = pigParts;
        this.maxWeight = maxWeight;
    }

    public int getTrayId() {
        return trayId;
    }

    public void setTrayId(int trayId) {
        this.trayId = trayId;
    }

    public List<PigPart> getPigParts() {
        return pigParts;
    }

    public void setPigParts(List<PigPart> pigParts) {
        this.pigParts = pigParts;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void addPigPart(PigPart part){
        if (part.getPartWeight()+getCurrentWeight()<maxWeight){
            pigParts.add(part);
        }
        else System.out.println("Cannot add part as it would make tray get beyond max weight ");

    }

    public int numberOfParts(){
        return pigParts.size();
    }

    public double getCurrentWeight(){
        double returnDouble = 0;
        for (PigPart part:pigParts) {
            returnDouble += part.getPartWeight();
        }
        return returnDouble;
    }

}
