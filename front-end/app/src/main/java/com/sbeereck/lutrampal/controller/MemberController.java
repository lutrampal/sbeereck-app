package com.sbeereck.lutrampal.controller;

import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.School;

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
        List<Object> jsonList = getDataManager().getArray("/members");
        List<Member> members = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Object obj : jsonList) {
            JSONObject jsonObject = (JSONObject) obj;
            members.add(new Member(((Number) jsonObject.get("id")).intValue(),
                    (String) jsonObject.get("first_name"),
                    (String) jsonObject.get("last_name"),
                    ((Number) jsonObject.get("balance")).floatValue(),
                    df.parse((String) jsonObject.get("last_membership_payment")),
                    (boolean) jsonObject.get("is_former_staff")));
        }
        return members;
    }

    private Map<String, Object> memberToJsonObject(Member member) {
        Map<String, Object> jsonMember = new HashMap<>();
        jsonMember.put("first_name", member.getFirstName());
        jsonMember.put("last_name", member.getLastName());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        jsonMember.put("last_membership_payment", df.format(member.getLastMembershipPayment()));
        jsonMember.put("balance", member.getBalance());
        jsonMember.put("email", member.getEmail());
        jsonMember.put("id", member.getId());
        jsonMember.put("phone", member.getPhone());
        jsonMember.put("school", member.getSchool().toString());
        jsonMember.put("is_former_staff", member.isFormerStaff());
        return jsonMember;
    }

    public Member getMember(int id) throws Exception {
        Map<String, Object> jsonMember = getDataManager().getObject("/members/" + id);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Member member = new Member(id, (String) jsonMember.get("first_name"),
                (String) jsonMember.get("last_name"),
                ((Number) jsonMember.get("balance")).floatValue(),
                df.parse((String) jsonMember.get("last_membership_payment")),
                (boolean) jsonMember.get("is_former_staff"),
                (String) jsonMember.get("phone"),
                (String) jsonMember.get("email"),
                School.getSchool((String) jsonMember.get("school"))
        );
        return member;
    }

    public void deleteMember(int id) throws Exception {
        getDataManager().delete("/members/" + id);
    }

    public int addMember(Member member) throws Exception {
        Map<String, Object> jsonMember = memberToJsonObject(member);
        return ((Number) getDataManager().post("/members", jsonMember).get("id")).intValue();
    }

    public void renewMembership(int id) throws Exception {
        getDataManager().put("/members/" + id + "/membership", null);
    }
}
