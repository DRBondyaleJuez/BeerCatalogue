import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.web.WebService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Objects;

public class WebServiceTests {

    private WebService testedWebService;
    private Manufacturer exampleManufacturer1;
    private Manufacturer exampleManufacturer2;
    private Beer exampleBeer1;
    private Beer exampleBeer2;

    private Beer exampleBeer3;


    @BeforeEach
    public void setUp(){
        testedWebService = new WebService();
        exampleManufacturer1 = new Manufacturer("Heineken","Germany");
        exampleManufacturer2 = new Manufacturer("Amstel","Netherlands");
        exampleBeer1 = new Beer("Heineken Lager",10.5,"Yellow","Soft Beer with little flavour but very refreshing.", exampleManufacturer1);
        exampleBeer2 = new Beer("Heineken Dark",20.3,"Black","Strong a very flavourful beer.", exampleManufacturer1);
        exampleBeer3 = new Beer("Amstel Double Zero",0.0,"Light","Beer low on calories without alcohol but maintaining flavour", exampleManufacturer2);
    }


    //Testing adding related endpoints

    //Test Adding one manufacturer
    @Test
    public void addOneManufacturerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewManufacturerResponse = testedWebService.addNewManufacturer(exampleManufacturer1);

        Assertions.assertEquals(HttpStatus.OK,addNewManufacturerResponse.getStatusCode(),"System was unable to add Manufacturer");

    }

    //Test Adding the same manufacturer
    @Test
    public void addSameManufacturerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewManufacturerResponse = testedWebService.addNewManufacturer(exampleManufacturer1);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE,addNewManufacturerResponse.getStatusCode(),"System added a beer which was already added");

    }

    //Test Adding a different manufacturer
    @Test
    public void addDifferentManufacturerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewManufacturerResponse = testedWebService.addNewManufacturer(exampleManufacturer2);

        Assertions.assertEquals(HttpStatus.OK,addNewManufacturerResponse.getStatusCode(),"System unable to add a different manufacturer from the presents");

    }

    //Test Adding one beer
    @Test
    public void addOneBeerTest(){

        //Beer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(exampleBeer1);

        Assertions.assertEquals(HttpStatus.OK,addNewBeerResponse.getStatusCode(),"System was unable to add Beer");

    }

    //Test Adding the same Beer
    @Test
    public void addSameBeerTest(){

        //Beer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(exampleBeer1);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE,addNewBeerResponse.getStatusCode(),"System added a beer which was already added");

    }

    //Test Adding a different beer
    @Test
    public void addDifferentBeerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(exampleBeer2);

        Assertions.assertEquals(HttpStatus.OK,addNewBeerResponse.getStatusCode(),"System unable to add a different beer from the presents");

    }


    // Test Retrieving

    // Test retrieving all manufacturers
    @Test
    public void retrieveAllManufacturersTest(){

        //Manufacturer retrieval

        ResponseEntity<ArrayList<String>> getAllManufacturersResponse = testedWebService.getManufacturerList();

        Assertions.assertEquals(HttpStatus.OK,getAllManufacturersResponse.getStatusCode(),"System was unable to retrieve list of Manufacturers");

        Assertions.assertEquals(2,getAllManufacturersResponse.getBody().size());

        //compare return manufacturer list
        boolean compareFirstManufacturer = exampleManufacturer1.getName().equals(getAllManufacturersResponse.getBody().get(1));
        Assertions.assertTrue(compareFirstManufacturer, "The manufacturer name - " + getAllManufacturersResponse.getBody().get(1) + " - does no equal the expected name - " + exampleManufacturer1.getName());

        boolean compareSecondManufacturer = exampleManufacturer2.getName().equals(getAllManufacturersResponse.getBody().get(0));
        Assertions.assertTrue(compareSecondManufacturer, "The manufacturer name - " + getAllManufacturersResponse.getBody().get(0) + " - does no equal the expected name - " + exampleManufacturer2.getName());

    }

    // Test retrieving all beers
    @Test
    public void retrieveAllBeersTest(){

        //Beer retrieval

        ResponseEntity<ArrayList<String>> getAllBeersResponse = testedWebService.getBeerList();

        Assertions.assertEquals(HttpStatus.OK,getAllBeersResponse.getStatusCode(),"System was unable to retrieve list of Beers");
        Assertions.assertEquals(2,getAllBeersResponse.getBody().size());

        //compare return beer list
        boolean compareFirstBeer = exampleBeer1.getName().equals(getAllBeersResponse.getBody().get(1));
        Assertions.assertTrue(compareFirstBeer, "The beer name - " + getAllBeersResponse.getBody().get(1) + " - does no equal the expected name - " + exampleBeer1.getName());

        boolean compareSecondBeer = exampleBeer2.getName().equals(getAllBeersResponse.getBody().get(0));
        Assertions.assertTrue(compareSecondBeer, "The beer name - " + getAllBeersResponse.getBody().get(0) + " - does no equal the expected name - " + exampleBeer2.getName());

    }

    private boolean compareBeers(Beer beer1, Beer beer2){
        return beer1.getName().equals(beer2.getName()) && beer1.getManufacturer().getName().equals(beer2.getManufacturer().getName());
    }

    //Testing finding a particular Manufacturer
    @Test
    public void retrieveParticularManufacturerTest(){

        //Manufacturer retrieval of a beer search using the name
        ResponseEntity<Manufacturer> findManufacturerResponse = testedWebService.getManufacturerDetail(exampleManufacturer1.getName());

        Assertions.assertEquals(HttpStatus.OK,findManufacturerResponse.getStatusCode(),"System was unable to retrieve named manufacturer");

        //compare return beer list
        boolean compareManufacturer = exampleManufacturer1.getName().equals(findManufacturerResponse.getBody().getName());
        Assertions.assertTrue(compareManufacturer, "The manufacturer name - " + findManufacturerResponse.getBody().getName() + " - does no equal the expected name - " + exampleManufacturer1.getName());

    }

    //Testing finding a particular Manufacturer
    @Test
    public void retrieveParticularBeerTest(){

        //Manufacturer retrieval of a beer search using the name
        ResponseEntity<ArrayList<Beer>> findBeerResponse = testedWebService.getBeerDetails(exampleBeer1.getName());

        Assertions.assertEquals(HttpStatus.OK,findBeerResponse.getStatusCode(),"System was unable to retrieve named Beer");

        //compare return beer list
        boolean compareBeer = compareBeers(exampleBeer1,findBeerResponse.getBody().get(0));
        Assertions.assertTrue(compareBeer, "The beer name - " + findBeerResponse.getBody().get(0).getName() + " - does no equal the expected name - " + exampleManufacturer1.getName());

    }


    //Test adding several beers and retrieving all








    //Testing Manufacturer related endpoints




}
