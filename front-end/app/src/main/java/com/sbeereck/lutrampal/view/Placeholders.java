package com.sbeereck.lutrampal.view;

import com.sbeereck.lutrampal.controller.RESTDataManager;
import com.sbeereck.lutrampal.controller.RESTfulDataManager;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;
import com.sbeereck.lutrampal.model.School;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lutrampal on 26/12/2017.
 */

public class Placeholders {

    public static RESTDataManager getPlaceHolderDataManager() {
        return new RESTfulDataManager("192.168.1.42", "8081", "0001password");
    }

}