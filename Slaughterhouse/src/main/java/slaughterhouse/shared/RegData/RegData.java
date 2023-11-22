package slaughterhouse.shared.RegData;

import slaughterhouse.shared.Pig;

import java.time.LocalDate;
import java.util.Collection;

public interface RegData
{
    public Pig create(double weight, String farm);
    public Pig read(int registrationNumber);
    public Collection<Pig> readAll();
    public Collection<Pig> readAllWithFarmAndDate(String farm, LocalDate date);
    public Collection<Pig> getReadyPigs();
    public int update(Pig pig);
    public void delete(int registrationNumber);
}
