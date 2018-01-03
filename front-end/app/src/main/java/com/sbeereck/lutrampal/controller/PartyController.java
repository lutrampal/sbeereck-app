package com.sbeereck.lutrampal.controller;

import com.sbeereck.lutrampal.model.Party;

import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        Object deletedParty = getDataManager().delete("/parties/" + partyId);
        if (deletedParty == null) {
            throw new Exception("Party not found");
        }
    }
}
