package com.sbeereck.lutrampal.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public class RESTfulDataManager implements RESTDataManager {

    private String host;
    private String port;
    private String authToken;

    public String getHost() {
        if (host.toLowerCase().startsWith("http://")) {
            return host;
        } else {
            return "http://" + host;
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    private String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public RESTfulDataManager(String host, String port, String authToken) {
        this.host = host;
        this.port = port;
        this.authToken = authToken;
    }

    @Override
    public Map<String, Object> getObject(String resource) throws Exception {
        Object res = sendHttpWithoutBody(resource, "GET");
        if (res instanceof JSONObject) {
            return (JSONObject) res;
        }
        return null;
    }

    @Override
    public List<Object> getArray(String resource) throws Exception {
        Object res = sendHttpWithoutBody(resource, "GET");
        if (res instanceof JSONArray) {
            return (JSONArray) res;
        }
        return null;
    }

    @Override
    public void post(String resource, Map<String, Object> jsonObject) {

    }

    @Override
    public void put(String resource, Map<String, Object> jsonObject) {

    }

    @Override
    public Map<String, Object> delete(String resource) throws Exception {
        Object res = sendHttpWithoutBody(resource, "DELETE");
        if (res instanceof JSONObject) {
            return (JSONObject) res;
        }
        return null;
    }

    private Object sendHttpWithoutBody(String resource, String method) throws Exception {
        Scanner scanner = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(getHost() + ":"
                    + getPort() + resource).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("authentication-token", getAuthToken());
            InputStream response = connection.getInputStream();
            scanner = new Scanner(response);
            String body = scanner.useDelimiter("\\A").next();
            JSONParser parser = new JSONParser();
            return parser.parse(body);
        } catch (Exception e) {
            throw e;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
