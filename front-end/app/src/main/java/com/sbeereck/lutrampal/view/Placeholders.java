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
        return new RESTfulDataManager("192.168.0.10", "8081", "0001password");
    }

    public static List<Product> getPlaceHolderProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Triple K", 2f, ProductType.BEER));
        products.add(new Product("Chouffe", 2.5f, ProductType.BEER));
        products.add(new Product("Caution pinte", 1f, ProductType.DEPOSIT));
        products.add(new Product("Caution pichet", 3f, ProductType.DEPOSIT));
        products.add(new Product("Saucisson", 3f, ProductType.FOOD));
        return products;
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

    public static List<Product> getPlaceHolderDeposits() {
        List<Product> deposits = new ArrayList<>();
        deposits.add(new Product("Caution demi", 1f, ProductType.DEPOSIT));
        deposits.add(new Product("Caution pinte", 2f, ProductType.DEPOSIT));
        deposits.add(new Product("Caution pichet", 3f, ProductType.DEPOSIT));
        return deposits;
    }

    public static List<Product> getPlaceHolderFood() {
        List<Product> food = new ArrayList<>();
        food.add(new Product("Saucisson", 3f, ProductType.FOOD));
        return food;
    }

    public static float getPlaceHolderDefaultNormalBeerPrice() {
        return 1.35f;
    }

    public static float getPlaceHolderDefaultSpecialBeerPrice() {
        return 1.70f;
    }

    public static float getPlaceHolderBalanceTooLowThreshold() { return -10; }
}