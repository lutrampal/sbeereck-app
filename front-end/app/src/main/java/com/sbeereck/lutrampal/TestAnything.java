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
        PartyController controller = new PartyController(dm);
        Party party = controller.getParty(1);
        System.out.println(party);
        System.out.println(party.getServedBeers());
        party.setName("edited name");
        controller.editParty(party);
        System.out.println(controller.getParty(1));
    }
}
