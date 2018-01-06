package com.sbeereck.lutrampal.controller;

import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;
import com.sbeereck.lutrampal.view.R;

import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public List<Product> getAllProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        for (ProductType type : ProductType.values()) {
            products.addAll(getProductsByType(type));
        }
        return products;
    }

    public void deleteProduct(int id) throws Exception {
        getDataManager().delete("/products/" + id);
    }

    public int addProduct(Product product) throws Exception {
        Map<String, Object> jsonProduct = productToJsonObject(product);
        Map<String, Object> res = getDataManager().post("/products", jsonProduct);
        if (res != null && res.containsKey("id")) {
            return ((Number)res.get("id")).intValue();
        }
        return -1;
    }

    private Map<String, Object> productToJsonObject(Product product) {
        Map<String, Object> jsonProduct = new HashMap<>();
        jsonProduct.put("name", product.getName());
        jsonProduct.put("price", product.getPrice());
        jsonProduct.put("type", product.getType().toString());
        return jsonProduct;
    }
}
