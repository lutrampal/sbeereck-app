package com.sbeereck.lutrampal;

import com.sbeereck.lutrampal.controller.MemberController;
import com.sbeereck.lutrampal.controller.PartyController;
import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.controller.RESTfulDataManager;
import com.sbeereck.lutrampal.controller.TransactionController;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Transaction;

import java.util.List;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public class TestAnything {

    public static void main(String[] args) throws Exception {
        RESTDataManager dm = new RESTfulDataManager("localhost", "8081", "0001password");
        TransactionController tController = new TransactionController(dm);
        PartyController pController = new PartyController(dm);
        Party p = pController.getParty(1);
        List<Transaction> transactions = tController.getTransactionsForParty(p);
        System.out.println(transactions);
    }
}
