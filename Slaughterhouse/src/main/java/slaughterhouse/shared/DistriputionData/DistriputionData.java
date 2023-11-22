package slaughterhouse.shared.DistriputionData;
import slaughterhouse.shared.Package;
import slaughterhouse.shared.PigPart;
import slaughterhouse.shared.Tray;

import java.util.Collection;
import java.util.List;

public interface DistriputionData {
    public PigPart createPigPart(int pigReg, String partName, double partWeight);
    public PigPart readPigPart (int partNumber);
    public Collection<PigPart> readAllPigParts(int regNumber);
    public Collection<PigPart> readAllWithoutTray();
    public Collection<PigPart> readAllWithoutPackage();

    //tray and/or package can be null
    public int updatePigPart(PigPart pigPart, Tray tray, Package _package);
    public void deletePigPart(int partNumber);

    public Tray createTray(List<PigPart> pigParts, int maxWeight);
    public Tray readTray (int trayId);
    public Collection<Tray> readAllPigTrays();
    public int updateTray(Tray tray);
    public void deleteTray(int trayId);

    public Package createPackage(List<PigPart> pigParts, String packageType);
    public Package readPackage (int packageId);
    public Collection<Package> readAllPackages();
    public Collection<Integer> readRegNumbersInPackage(int packageId);
    public int updatePackage(Package _package);
    public void deletePackage(int packageId);
    public Collection<Integer> readPackagesFromRegNumber(int regNumber);
}
