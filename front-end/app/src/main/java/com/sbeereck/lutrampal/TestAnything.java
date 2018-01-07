package com.sbeereck.lutrampal;

import com.sbeereck.lutrampal.controller.MemberController;
import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.controller.RESTfulDataManager;
import com.sbeereck.lutrampal.model.Member;

import java.util.List;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public class TestAnything {

    public static void main(String[] args) throws Exception {
        RESTDataManager dm = new RESTfulDataManager("localhost", "8081", "0001password");
        MemberController controller = new MemberController(dm);
        List<Member> members = controller.getAllMembers();
        System.out.println(members);
    }
}
