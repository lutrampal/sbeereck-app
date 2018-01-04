package com.sbeereck.lutrampal.controller;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
        Object res = sendHttp(resource, "GET", null);
        if (res instanceof JSONObject) {
            return (JSONObject) res;
        }
        return null;
    }

    @Override
    public List<Object> getArray(String resource) throws Exception {
        Object res = sendHttp(resource, "GET", null);
        if (res instanceof JSONArray) {
            return (JSONArray) res;
        }
        return null;
    }

    /**
     * Send the given object to the desired resource with a POST request
     * @param resource the REST resource to target
     * @param jsonObject a map of values to convert to a json string. The values may be any valid
     */
    @Override
    public Map<String, Object> post(String resource, Object jsonObject) throws Exception {
        Gson gson = new Gson();
        Object res = sendHttp(resource, "POST", gson.toJson(jsonObject));
        if (res instanceof JSONObject) {
            return (JSONObject) res;
        }
        return null;
    }

    /**
     * Send the given object to the desired resource with a PUT request
     * @param resource the REST resource to target
     * @param jsonObject a map of values to convert to a json string. The values may be any valid
     */
    @Override
    public Map<String, Object> put(String resource, Object jsonObject) throws Exception {
        Gson gson = new Gson();
        Object res = sendHttp(resource, "PUT", gson.toJson(jsonObject));
        if (res instanceof JSONObject) {
            return (JSONObject) res;
        }
        return null;

    }

    @Override
    public Map<String, Object> delete(String resource) throws Exception {
        Object res = sendHttp(resource, "DELETE", null);
        if (res instanceof JSONObject) {
            return (JSONObject) res;
        }
        return null;
    }

    private Object sendHttp(String resource, String method, String json) throws Exception {
        Scanner scanner = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(getHost() + ":"
                    + getPort() + resource).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("authentication-token", getAuthToken());
            if (json != null) {
                connection.setRequestProperty("content-type", "application/json");
                writeBody(connection, json);
            }
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

    private void writeBody(HttpURLConnection connection, String body) throws Exception {
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(body);
        osw.flush();
        osw.close();
        os.close();
        connection.connect();
    }
}
