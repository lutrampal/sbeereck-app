package com.sbeereck.lutrampal.controller;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;

import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public class PartyController {

    private RESTDataManager dataManager;

    public RESTDataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public PartyController(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Party> getAllParties() throws Exception {
        List<Object> jsonList = getDataManager().getArray("/parties");
        List<Party> parties = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Object obj : jsonList) {
            JSONObject jsonObject = (JSONObject) obj;
            parties.add(new Party(((Number)jsonObject.get("id")).intValue(),
                    (String) jsonObject.get("name"),
                    df.parse((String)jsonObject.get("date")),
                    ((Number)jsonObject.get("number_of_attendees")).intValue(),
                    ((Number)jsonObject.get("balance")).floatValue()));
        }
        return parties;
    }

    public void deleteParty(int partyId) throws Exception {
        getDataManager().delete("/parties/" + partyId);
    }

    public int addParty(Party party) throws Exception {
        Map<String, Object> jsonParty = partyToJsonObject(party);
        Map<String, Object> res = getDataManager().post("/parties", jsonParty);
        if (res != null && res.containsKey("id")) {
            return ((Number)res.get("id")).intValue();
        }
        return -1;
    }

    private Map<String, Object> partyToJsonObject(Party party) {
        Map<String, Object> jsonParty = new HashMap<>();
        jsonParty.put("name", party.getName());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        jsonParty.put("date", df.format(party.getDate()));
        jsonParty.put("normal_beer_price", party.getNormalBeerPrice());
        jsonParty.put("special_beer_price", party.getSpecialBeerPrice());
        List<Map<String, Object>> jsonServedBeers = new LinkedList<>();
        for (Product beer : party.getServedBeers().keySet()) {
            Map<String, Object> jsonServedBeer = new HashMap<>();
            jsonServedBeer.put("id", beer.getId());
            jsonServedBeer.put("category", party.getServedBeers().get(beer).toString());
            jsonServedBeers.add(jsonServedBeer);
        }
        jsonParty.put("served_beers", jsonServedBeers);
        return jsonParty;
    }

    public void editParty(Party party) throws Exception {
        Map<String, Object> jsonParty = partyToJsonObject(party);
        getDataManager().put("/parties/" + party.getId(), jsonParty);
    }

    public Party getParty(int id) throws Exception {
        Map<String, Object> jsonParty = getDataManager().getObject("/parties/" + id);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map<Product, BeerCategory> servedBeers = new HashMap<>();
        for (Map<String, Object> value : (List<Map<String, Object>>) jsonParty.get("served_beers")) {
            Product beer = new Product(((Number)value.get("id")).intValue(),
                    (String) value.get("name"),
                    ((Number) value.get("price")).floatValue(),
                    ProductType.BEER);
            String category = (String) value.get("category");
            BeerCategory beerCategory;
            if (category.equals("normal")) {
                beerCategory = BeerCategory.NORMAL;
            } else {
                beerCategory = BeerCategory.SPECIAL;
            }
            servedBeers.put(beer, beerCategory);
        }
        Party party = new Party((String) jsonParty.get("name"),
                df.parse((String) jsonParty.get("date")),
                ((Number)jsonParty.get("normal_beer_price")).floatValue(),
                ((Number)jsonParty.get("special_beer_price")).floatValue(),
                servedBeers);
        party.setId(id);
        return party;
    }
}
