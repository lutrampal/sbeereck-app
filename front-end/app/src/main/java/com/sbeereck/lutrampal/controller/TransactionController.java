package com.sbeereck.lutrampal.controller;

import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Transaction;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lutrampal on 08/01/18 for S'Beer Eck.
 */

public class TransactionController {
    private RESTDataManager dataManager;

    public RESTDataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public TransactionController(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Transaction> getTransactionsForParty(Party party) throws Exception {
        List<Object> jsonList = getDataManager().getArray("/parties/" + party.getId()
                + "/transactions");
        List<Transaction> transactions = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (Object obj : jsonList) {
            JSONObject jsonObject = (JSONObject) obj;
            Member member = new Member(((Number)jsonObject.get("member_id")).intValue(),
                    (String) jsonObject.get("first_name"),
                    (String) jsonObject.get("last_name"));
            transactions.add(new Transaction(((Number)jsonObject.get("id")).intValue(),
                    member, party,
                    ((Number)jsonObject.get("amount")).floatValue(),
                    (String)jsonObject.get("label"),
                    df.parse((String)jsonObject.get("timestamp"))));
        }
        return transactions;
    }

    public void deleteTransaction(int id) throws Exception {
        getDataManager().delete("/transactions/" + id);
    }

    private Map<String, Object> transactionToJsonObject(Transaction transaction) {
        Map<String, Object> jsonTransaction = new HashMap<>();
        jsonTransaction.put("member_id", transaction.getMember().getId());
        if (transaction.getParty() != null && transaction.getParty().getId() != -1) {
            jsonTransaction.put("party_id", transaction.getParty().getId());
        }
        jsonTransaction.put("label", transaction.getLabel());
        jsonTransaction.put("amount", transaction.getAmount());
        return jsonTransaction;
    }

    public int addTransaction(Transaction transaction) throws Exception {
        Map<String, Object> res = getDataManager().post("/transactions",
                transactionToJsonObject(transaction));
        if (res != null && res.containsKey("id")) {
            return ((Number)res.get("id")).intValue();
        }
        return -1;
    }

    public float getBalanceThreshold() throws Exception {
        Map<String, Object> res = getDataManager().getObject("/balance_too_low_threshold");
        if (res != null && res.containsKey("balance_too_low_threshold")) {
            return ((Number)res.get("balance_too_low_threshold")).floatValue();
        }
        return 0;
    }

    public void updateBalanceThreshold(float newThreshold) throws Exception {
        Map<String, Object> jsonReq = new HashMap<>();
        jsonReq.put("balance_too_low_threshold", newThreshold);
        getDataManager().put("/balance_too_low_threshold", jsonReq);
    }
}
