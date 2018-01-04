package com.sbeereck.lutrampal;

import com.google.gson.Gson;
import com.sbeereck.lutrampal.controller.PartyController;
import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.controller.RESTfulDataManager;
import com.sbeereck.lutrampal.model.Party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public class TestAnything {

    public static void main(String[] args) throws Exception {
        RESTDataManager dm = new RESTfulDataManager("localhost", "8081", "0001password");
        Map<String, Object> testParty = new HashMap<>();
        testParty.put("name", "test");
        testParty.put("date", "2018-01-15");
        testParty.put("normal_beer_price", 1.35f);
        testParty.put("special_beer_price", 1.70f);
        List<Map<String, Object>> servedBeers = new ArrayList<>();
        testParty.put("served_beers", servedBeers);
        Map<String, Object> beer1 = new HashMap<>();
        beer1.put("id", 1);
        beer1.put("category", "special");
        Map<String, Object> beer2 = new HashMap<>();
        beer2.put("id", 2);
        beer2.put("category", "normal");
        servedBeers.add(beer1);
        servedBeers.add(beer2);
        Gson gson = new Gson();
        Map<String, Object> res = dm.post("/parties", testParty);
        for (String key : res.keySet()) {
            System.out.println(res.get(key));
        }
    }
}
