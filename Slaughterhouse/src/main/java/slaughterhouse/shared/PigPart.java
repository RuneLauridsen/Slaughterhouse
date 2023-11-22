package slaughterhouse.shared;

public class PigPart {
    private int partId;
    private int pigReg;
    private int trayId;
    private String partName;
    private double partWeight;

    public PigPart(int partId, int pigReg, int trayId, String partName, double partWeight) {
        this.partId = partId;
        this.pigReg = pigReg;
        this.trayId = trayId;
        this.partName = partName;
        this.partWeight = partWeight;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public int getPigReg() {
        return pigReg;
    }

    public void setPigReg(int pigReg) {
        this.pigReg = pigReg;
    }

    public int getTrayId() {
        return trayId;
    }

    public void setTrayId(int trayId) {
        this.trayId = trayId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public double getPartWeight() {
        return partWeight;
    }

    public void setPartWeight(double partWeight) {
        this.partWeight = partWeight;
    }
}
