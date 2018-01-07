package com.sbeereck.lutrampal.controller;

import com.sbeereck.lutrampal.model.Member;

import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lutrampal on 07/01/2018.
 */

public class MemberController {
    private RESTDataManager dataManager;

    public MemberController(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public RESTDataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Member> getAllMembers() throws Exception {
        List<Object> jsonList = getDataManager().getArray("/parties");
        List<Member> members = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Object obj : jsonList) {
            JSONObject jsonObject = (JSONObject) obj;
            members.add(new Member(((Number) jsonObject.get("id")).intValue(),
                    (String) jsonObject.get("first_name"),
                    (String) jsonObject.get("last_name"),
                    ((Number) jsonObject.get("balance")).floatValue(),
                    df.parse((String) jsonObject.get("last_membership_payment"))));
        }
        return members;
    }
}
