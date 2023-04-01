package com.application.web.AuxiliaryClient;

import com.application.model.Beer;
import com.application.model.Manufacturer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PunkApiClient {

    private final Requester requester;

    private final String protocolAndHost;

    public PunkApiClient() {
        requester = new Requester();
        protocolAndHost = "https://api.punkapi.com";
    }

    public ArrayList<Beer> requestBeerData(String beerName){

        HashMap<String, String> queryParams = new HashMap<>();

        queryParams.put("beer_name",beerName);

        String path = "/v2/beers";

        return processResponse(requester.getMethod(protocolAndHost,path,queryParams));

    }

    private ArrayList<Beer> processResponse(HttpResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //This is necessary son non-defined attributes don't trigger an UnrecognizedPropertyException
        String jsonBody;
        List<AuxiliaryPunkBeer> listPunkBeers;
        try {
            jsonBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
            // TODO: As always handle this better
        }

        try {
            listPunkBeers = objectMapper.readValue(jsonBody, new TypeReference<List<AuxiliaryPunkBeer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Beer> beerSearched = new ArrayList<>();

        Manufacturer auxiliaryManufacturer = new Manufacturer("PunkApi","External Manufacturer");

        for (AuxiliaryPunkBeer apBeer : listPunkBeers) {
            String tempName = apBeer.getName();
            double tempGraduation = apBeer.getAbv();
            String tempType = apBeer.getTagline();
            String tempDescription = apBeer.getDescription();

            Beer tempBeer = new Beer(tempName,tempGraduation,tempType,tempDescription,auxiliaryManufacturer);
            beerSearched.add(tempBeer);
        }

        return beerSearched;
    }

}
