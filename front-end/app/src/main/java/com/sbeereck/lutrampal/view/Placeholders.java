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
        return new RESTfulDataManager("192.168.1.6", "8081", "0001password");
    }

    public static List<Product> getPlaceHolderBeers() {
        List<Product> beers = new ArrayList<>();
        beers.add(new Product("Triple K", 2.5f, ProductType.BEER));
        beers.add(new Product("Chouffe", 3f, ProductType.BEER));
        beers.add(new Product("Sassenage blonde", 1.5f, ProductType.BEER));
        beers.add(new Product("Sassenage rousse", 1.5f, ProductType.BEER));
        beers.add(new Product("Punk IPA", 1.5f, ProductType.BEER));
        return beers;
    }

    public static float getPlaceHolderDefaultNormalBeerPrice() {
        return 1.35f;
    }

    public static float getPlaceHolderDefaultSpecialBeerPrice() {
        return 1.70f;
    }

    public static float getPlaceHolderBalanceTooLowThreshold() { return -10; }
}