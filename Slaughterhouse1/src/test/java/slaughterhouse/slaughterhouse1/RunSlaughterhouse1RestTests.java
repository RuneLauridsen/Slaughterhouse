package slaughterhouse.slaughterhouse1;

import slaughterhouse.shared.Pig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
//import org.apache.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import slaughterhouse.shared.TestUtil;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// spring.io/guides/gs/testing-web/
@SpringBootTest(classes = RunSlaughterhouse1Rest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RunSlaughterhouse1RestTests {

    private Pig request;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void init() {
        request = new Pig(1234, 0, "Jens Hansens Bondegård");
        TestUtil.resetSqlDatabase();
    }

    @Test
    void getPigRegNr() {

        ResponseEntity<Pig> response = restTemplate.getForEntity("http://localhost:8080/pigs/1", Pig.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Simons Farm", response.getBody().getFarm());
        assertEquals(40.0, response.getBody().getWeight());
        assertEquals(1, response.getBody().getRegistrationNumber());
    }

    @Test
    void getAll() {
        ResponseEntity<Pig[]> response = restTemplate.getForEntity("http://localhost:8080/pigs", Pig[].class);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(4, response.getBody().length);
    }

    @Test
    void getByDate() {
        DateTimeFormatter yyyymmdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        {
            ResponseEntity<Pig[]> response = restTemplate.getForEntity("http://localhost:8080/pigs?date=2023-10-01", Pig[].class);
            assertEquals(200, response.getStatusCode().value());
            assertEquals(1, response.getBody().length);
        }

        {
            ResponseEntity<Pig[]> response = restTemplate.getForEntity("http://localhost:8080/pigs?date=2050-01-01", Pig[].class);
            assertEquals(200, response.getStatusCode().value());
            assertEquals(0, response.getBody().length);
        }
    }

    @Test
    void getByFarm() {
        ResponseEntity<Pig[]> response = restTemplate.getForEntity("http://localhost:8080/pigs?farm=Simons Farm", Pig[].class);
        assertEquals(response.getStatusCode().value(), 200);
        Pig[] pigs = response.getBody();
        assertEquals(pigs[0].getFarm(), "Simons Farm");
    }

    @Test
    void stealAPig() {
        // GET før PUT
        ResponseEntity<Pig> response = restTemplate.getForEntity("http://localhost:8080/pigs/1", Pig.class);
        Pig pig = response.getBody();
        assertEquals("Simons Farm", pig.getFarm());

        // PUT
        pig.setFarm("Runes farm");
        restTemplate.put("http://localhost:8080/pigs/1", pig);

        // GET efter PUT
        response = restTemplate.getForEntity("http://localhost:8080/pigs/1", Pig.class);
        pig = response.getBody();
        assertEquals("Runes farm", pig.getFarm());
    }

    @Test
    void killAPig() {

        // Test at gris 1 findes i forvejen.
        {
            ResponseEntity<Pig> response = restTemplate.getForEntity("http://localhost:8080/pigs/1", Pig.class);
            assertEquals(200, response.getStatusCode().value());
        }

        // Test at grid 1 ikke findes, efter den er slettet.
        {
            restTemplate.delete("http://localhost:8080/pigs/1", Pig.class);
            ResponseEntity<Pig> response = restTemplate.getForEntity("http://localhost:8080/pigs/1", Pig.class);
            assertEquals(404, response.getStatusCode().value());
        }
    }

    @Test
    void createPig() {

        {
            ResponseEntity<Pig> response = restTemplate.postForEntity("http://localhost:8080/pigs", request, Pig.class);

            assertEquals(200, response.getStatusCode().value());
            assertEquals("Jens Hansens Bondegård", response.getBody().getFarm());
            assertEquals(1234.0, response.getBody().getWeight());
            assertEquals(5, response.getBody().getRegistrationNumber());

            // TODO: Et "DateService" interface på server-siden, så test resultat ikke afhænger i dags dato.
            // Pt. kan vi ikke test response.getBody().getDate()
        }

        {
            ResponseEntity<Pig> response = restTemplate.getForEntity("http://localhost:8080/pigs/2", Pig.class);
            assertEquals(200, response.getStatusCode().value());
            assertNotEquals(null, response.getBody());
        }
    }
}
