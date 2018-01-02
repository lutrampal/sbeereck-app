package com.sbeereck.lutrampal;

import com.sbeereck.lutrampal.controller.PartyController;
import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.controller.RESTfulDataManager;
import com.sbeereck.lutrampal.model.Party;

import java.util.List;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public class TestAnything {

    public static void main(String[] args) throws Exception {
        RESTDataManager dm = new RESTfulDataManager("localhost", "8081", "0001password");
        PartyController ctrl = new PartyController(dm);
        List<Party> parties = ctrl.getAllParties();
        for (Party p : parties) {
            System.out.println(p);
        }
    }
}
