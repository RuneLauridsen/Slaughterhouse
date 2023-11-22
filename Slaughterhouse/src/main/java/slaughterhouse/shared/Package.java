package slaughterhouse.shared;

import java.util.List;

public class Package {
    private int packageId;
    private String packageType;
    private List<PigPart> parts;

    // Liste med trays, kan findes ved at gå fra PigPart -> Tray (normalform?)

    public Package(int packageId, String packageType, List<PigPart> parts) {
        this.packageId = packageId;
        this.packageType = packageType;
        this.parts = parts;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public List<PigPart> getParts() {
        return parts;
    }

    public void setParts(List<PigPart> parts) {
        this.parts = parts;
    }
}
