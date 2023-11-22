package slaughterhouse.shared.RegData;

import slaughterhouse.shared.Pig;

import java.time.LocalDate;
import java.util.*;

//@Component NOTE(rune): Bruger PigDataDatabase is stedet.
public class RegDataInMemory implements RegData
{
    public Map<Integer, Pig> data;
    private int lastRegistrationNumber;

    public RegDataInMemory()
    {
        data = new HashMap<>();
        lastRegistrationNumber = 0;

        create(54321, "Simons farm");
    }

    public Pig create(double weight, String farm)
    {
        int registrationNumber = ++lastRegistrationNumber;

        Pig pig = new Pig(weight, registrationNumber, farm, LocalDate.now(), null);
        data.put(registrationNumber, pig);
        return pig;
    }

    public Pig read(int registrationNumber)
    {
        Pig pig = data.getOrDefault(registrationNumber, null);
        return pig;
    }

    public Collection<Pig> readAll()
    {
        Collection<Pig> ret = data.values();
        return ret;
    }

    public Collection<Pig> readAllWithFarmAndDate(String farm, LocalDate date)
    {
        List<Pig> result = new ArrayList<>();

        for(Pig pig : data.values())
        {
            if(farm != null && !pig.getFarm().equals(farm))
            {
                continue;
            }

            if(date != null && !pig.getRegistrationDate().equals(date))
            {
                continue;
            }

            result.add(pig);
        }

        return result;
    }

    @Override
    public Collection<Pig> getReadyPigs() {
        Collection<Pig> results = new ArrayList<>();
        for(Pig p : data.values()) {
            if (p.getSplitDate() == null) {
                results.add(p);
            }
        }
        return results;
    }

    public int update(Pig pig)
    {
        if(data.containsKey(pig.getRegistrationNumber()))
        {
            data.put(pig.getRegistrationNumber(), pig);
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public void delete(int registrationNumber)
    {
        data.remove(registrationNumber);
    }
}
