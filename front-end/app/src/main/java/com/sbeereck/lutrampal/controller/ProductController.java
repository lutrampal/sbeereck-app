package com.sbeereck.lutrampal.controller;

import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;

import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lutrampal on 03/01/18 for S'Beer Eck.
 */

public class ProductController {

    private RESTDataManager dataManager;

    public RESTDataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public ProductController(RESTDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Product> getProductsByType(ProductType type) throws Exception {
        List<Object> jsonList = getDataManager().getArray("/products?type="
                + URLEncoder.encode(type.toString(), "UTF-8"));
        List<Product> products = new ArrayList<>();
        for (Object obj : jsonList) {
            JSONObject jsonObject = (JSONObject) obj;
            products.add(new Product(((Number)jsonObject.get("id")).intValue(),
                    (String) jsonObject.get("name"),
                    ((Number)jsonObject.get("price")).floatValue(),
                    type));
        }
        return products;

    }
}
