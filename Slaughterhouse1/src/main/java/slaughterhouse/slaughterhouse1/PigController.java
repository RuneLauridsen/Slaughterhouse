package slaughterhouse.slaughterhouse1;

import slaughterhouse.shared.Pig;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import slaughterhouse.shared.RegData.RegData;

import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.time.LocalDate;

@RestController
@RequestMapping("/pigs")
@Component
public class PigController
{
    private final RegData data;

    public PigController(RegData data) {
        this.data = data;
    }

    @PostMapping
    public Pig createPig(@RequestBody Pig pig) {
        Pig ret = data.create(pig.getWeight(), pig.getFarm());
        return ret;
    }

    @GetMapping("/{registrationNumber}")
    public Pig readPig(@PathVariable("registrationNumber") int registrationNumber) {
        Pig ret = data.read(registrationNumber);
        if (ret == null)
        {
            throw new NotFoundException();
        }
        return ret;
    }

    @GetMapping
    public Collection<Pig> readAllPigs(
            @RequestParam(required = false) String farm,
            @RequestParam(required = false) String date
    ) {
        try
        {
            LocalDate parsedDate = null;
            if(date != null)
            {
                parsedDate = LocalDate.parse(date);
            }

            Collection<Pig> ret = data.readAllWithFarmAndDate(farm, parsedDate);
            return ret;
        }
        catch (DateTimeParseException e)
        {
            throw new BadRequestException();
        }
    }

    @PutMapping("/{registrationNumber}")
    public void updatePig(@PathVariable("registrationNumber") int registrationNumber, @RequestBody Pig pig) {
        if(registrationNumber != pig.getRegistrationNumber()) {
            throw new BadRequestException();
        }

        int updated = data.update(pig);
        if (updated == 0) {
            throw new NotFoundException();
        }
    }

    @DeleteMapping("/{registrationNumber}")
    public void deletePig(@PathVariable("registrationNumber") int registrationNumber) {
        data.delete(registrationNumber);
    }
}
