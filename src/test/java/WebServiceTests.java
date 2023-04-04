import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.application.web.WebService;
import com.application.web.requests.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class WebServiceTests {

    private WebService testedWebService;
    private Manufacturer exampleManufacturer1;
    private Manufacturer exampleManufacturer2;
    private Beer exampleBeer1;
    private Beer exampleBeer2;

    private Beer exampleBeer3;
    private CreateNewUserRequest createNewUserRequest1;
    private CreateNewUserRequest createNewUserRequest2;
    private CreateNewUserRequest createNewUserRequestAdmin;
    private UUID adminToken;


    @BeforeEach
    public void setUp(){
        testedWebService = new WebService();
        exampleManufacturer1 = new Manufacturer("Heineken","Germany");
        exampleManufacturer2 = new Manufacturer("Amstel","Netherlands");
        exampleBeer1 = new Beer("Heineken Lager",10.5,"Yellow","Soft Beer with little flavour but very refreshing.", exampleManufacturer1);
        exampleBeer2 = new Beer("Heineken Dark",20.3,"Black","Strong a very flavourful beer.", exampleManufacturer1);
        exampleBeer3 = new Beer("Amstel Double Zero",0.0,"Light","Beer low on calories without alcohol but maintaining flavour", exampleManufacturer2);
        createNewUserRequest1 = new CreateNewUserRequest("username1","password1",exampleManufacturer1);
        createNewUserRequest2 = new CreateNewUserRequest("username1","password1",exampleManufacturer2);
        createNewUserRequestAdmin = new CreateNewUserRequest("userAdmin","passwordAdmin",new Manufacturer("admin","admin"));
        testedWebService.createNewUser(createNewUserRequestAdmin);
        adminToken = testedWebService.signIn(new SignInRequest("userAdmin","passwordAdmin")).getBody();
    }


    //Testing adding related endpoints

    //Test Adding one manufacturer
    @Test
    public void addOneManufacturerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewManufacturerResponse = testedWebService.addNewManufacturer(new AddNewManufacturerRequest(exampleManufacturer1,adminToken));

        Assertions.assertEquals(HttpStatus.OK,addNewManufacturerResponse.getStatusCode(),"System was unable to add Manufacturer");

    }

    //Test Adding the same manufacturer
    @Test
    public void addSameManufacturerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewManufacturerResponse = testedWebService.addNewManufacturer(new AddNewManufacturerRequest(exampleManufacturer1,adminToken));

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE,addNewManufacturerResponse.getStatusCode(),"System added a beer which was already added");

    }

    //Test Adding a different manufacturer
    @Test
    public void addDifferentManufacturerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewManufacturerResponse = testedWebService.addNewManufacturer(new AddNewManufacturerRequest(exampleManufacturer2,adminToken));

        Assertions.assertEquals(HttpStatus.OK,addNewManufacturerResponse.getStatusCode(),"System unable to add a different manufacturer from the presents");

    }

    //Test Adding one beer
    @Test
    public void addOneBeerTest(){

        //Beer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(new AddNewBeerRequest(exampleBeer1,adminToken));

        Assertions.assertEquals(HttpStatus.OK,addNewBeerResponse.getStatusCode(),"System was unable to add Beer");

    }

    //Test Adding the same Beer
    @Test
    public void addSameBeerTest(){

        //Beer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(new AddNewBeerRequest(exampleBeer1,adminToken));

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE,addNewBeerResponse.getStatusCode(),"System added a beer which was already added");

    }

    //Test Adding a different beer
    @Test
    public void addDifferentBeerTest(){

        //Manufacturer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(new AddNewBeerRequest(exampleBeer2,adminToken));

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

    //Testing finding a particular Beer
    @Test
    public void retrieveParticularBeerTest(){

        //Manufacturer retrieval of a beer search using the name
        ResponseEntity<ArrayList<Beer>> findBeerResponse = testedWebService.getBeerDetails(exampleBeer1.getName());

        Assertions.assertEquals(HttpStatus.OK,findBeerResponse.getStatusCode(),"System was unable to retrieve named Beer");

        //compare return beer list
        boolean compareBeer = compareBeers(exampleBeer1,findBeerResponse.getBody().get(0));
        Assertions.assertTrue(compareBeer, "The beer name - " + findBeerResponse.getBody().get(0).getName() + " - does no equal the expected name - " + exampleManufacturer1.getName());

    }


    //Testing updating manufacturer
    @Test
    public void updateManufacturerTest(){

        String newNationality = "Austria";

        //Build Old nad new Manufacturer
        Manufacturer oldManufacturer = testedWebService.getManufacturerDetail("Heineken").getBody();
        Manufacturer newManufacturer = new Manufacturer("Heineken",newNationality);

        //Update Manufacturer
        UpdateManufacturerInfoRequest updateManufacturerInfoRequest = new UpdateManufacturerInfoRequest(oldManufacturer,newManufacturer,adminToken);
        ResponseEntity<String> updateManufacturerResponse = testedWebService.updateManufacturerInfo(updateManufacturerInfoRequest);

        //Check response
        Assertions.assertEquals(HttpStatus.OK,updateManufacturerResponse.getStatusCode(),"System was unable to update Manufacturer");

        //Check Update Manufacturer
        Manufacturer updatedManufacturer = testedWebService.getManufacturerDetail("Heineken").getBody();

        boolean compareUpdate = newNationality.equals(updatedManufacturer.getNationality());
        Assertions.assertTrue(compareUpdate, "The manufacturer name - " + updatedManufacturer.getName() + " was updated to change nationality to '" + newNationality + "', but " +
                " the nationality of the updated manufacturer was " + updatedManufacturer.getNationality());
    }

    //Testing updating beer
    @Test
    public void updateBeerTest(){

        double newGraduation = 24.6;

        //Build Old and new Beer
        Beer oldBeer = testedWebService.getBeerDetails("Heineken Lager").getBody().get(0);
        Beer newBeer = new Beer("Heineken Lager",newGraduation, oldBeer.getType(), oldBeer.getDescription(), oldBeer.getManufacturer());

        //Update Manufacturer
        UpdateBeerInfoRequest updateBeerInfoRequest = new UpdateBeerInfoRequest(oldBeer,newBeer,adminToken);
        ResponseEntity<String> updateBeerResponse = testedWebService.updateBeerInfo(updateBeerInfoRequest);

        //Check response
        Assertions.assertEquals(HttpStatus.OK,updateBeerResponse.getStatusCode(),"System was unable to update Beer");

        //Check Update Manufacturer
        Beer updatedBeer = testedWebService.getBeerDetails("Heineken Lager").getBody().get(0);

        boolean compareUpdate = newGraduation == updatedBeer.getGraduation();
        Assertions.assertTrue(compareUpdate, "The manufacturer name - " + updatedBeer.getName() + " was updated to change graduation to '" + newGraduation + "', but " +
                " the graduation of the updated beer was " + updatedBeer.getGraduation());
    }

    //Testing scenario when requiring the use of the auxiliary client
    @Test
    public void findBeerUsingAuxiliaryClientTest(){

        ResponseEntity<ArrayList<Beer>> response = testedWebService.getBeerDetails("Buzz");

        boolean compareStatus = response.getStatusCode() == HttpStatus.TEMPORARY_REDIRECT;

        boolean compareBeerName = response.getBody().get(0).getName().equals("Buzz");

        Assertions.assertTrue(compareStatus, "The expected status was TEMPORARY_REDIRECT. However, the resulting status was: " + response.getStatusCode());

        Assertions.assertTrue(compareBeerName, "The beer searched was expected to be named Buzz. However the name of the beer found was:  " + response.getBody().get(0).getName());

    }

    //Testing user creation

    @Test
    public void createNewUserTest(){

        ResponseEntity<String> response = testedWebService.createNewUser(createNewUserRequest1);

        boolean compareStatus = response.getStatusCode() == HttpStatus.ACCEPTED;

        boolean compareBeerName = Objects.equals(response.getBody(), "New user created");

        Assertions.assertTrue(compareStatus, "The expected status was ACCEPTED. However, the resulting status was: " + response.getStatusCode());

        Assertions.assertTrue(compareBeerName, "The user was not inserted correctly in the table users or was not connected to the manufacturer");

    }

    @Test
    public void signInFailTest(){

        ResponseEntity<UUID> response = testedWebService.signIn(new SignInRequest("fakeUsername","fakePassword"));

        boolean compareStatus = response.getStatusCode() == HttpStatus.UNAUTHORIZED;

        boolean compareToken = Objects.equals(response.getBody(), null);

        Assertions.assertTrue(compareStatus, "The expected status was UNAUTHORIZED. However, the resulting status was: " + response.getStatusCode());

        Assertions.assertTrue(compareToken, "The token should have been null but it was not");
    }

    @Test
    public void signInSuccessTest(){

        ResponseEntity<UUID> response = testedWebService.signIn(new SignInRequest("username1","password1"));

        boolean compareStatus = response.getStatusCode() == HttpStatus.ACCEPTED;

        boolean compareToken = Objects.equals(response.getBody(), null);
        System.out.println(response.getBody());

        Assertions.assertTrue(compareStatus, "The expected status was ACCEPTED. However, the resulting status was: " + response.getStatusCode());

        Assertions.assertFalse(compareToken, "The token should not have been null but it was");
    }

    @Test
    public void signInSuccessAgainTest(){

        ResponseEntity<UUID> response = testedWebService.signIn(new SignInRequest("username1","password1"));

        boolean compareStatus = response.getStatusCode() == HttpStatus.ACCEPTED;

        boolean compareToken = Objects.equals(response.getBody(), null);
        System.out.println(response.getBody());

        Assertions.assertTrue(compareStatus, "The expected status was ACCEPTED. However, the resulting status was: " + response.getStatusCode());

        Assertions.assertFalse(compareToken, "The token should not have been null but it was");
    }

    @Test
    public void signInAndAddBeerTest(){

        ResponseEntity<UUID> respnoseToken = testedWebService.signIn(new SignInRequest("username1","password1"));

        Beer newBeer = new Beer("Heineken Test",25.5,"Citrus","A beer that must be tried",exampleManufacturer1);
        AddNewBeerRequest addNewBeerRequest = new AddNewBeerRequest(newBeer,respnoseToken.getBody());

        //Beer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(addNewBeerRequest);

        Assertions.assertEquals(HttpStatus.OK,addNewBeerResponse.getStatusCode(),"System was unable to add Beer");
    }

    @Test
    public void signInAndAddUnauthorizedBeerTest(){

        ResponseEntity<UUID> respnoseToken = testedWebService.signIn(new SignInRequest("username1","password1"));

        Beer newBeer = new Beer("Heineken Test",25.5,"Citrus","A beer that must be tried",exampleManufacturer2);
        AddNewBeerRequest addNewBeerRequest = new AddNewBeerRequest(newBeer,respnoseToken.getBody());

        //Beer addition
        ResponseEntity<String> addNewBeerResponse = testedWebService.addNewBeer(addNewBeerRequest);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED,addNewBeerResponse.getStatusCode(),"System allowed beer insertion even though it should not or returned wrong status");
    }

}
