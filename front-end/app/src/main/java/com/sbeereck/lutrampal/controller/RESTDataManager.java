package com.sbeereck.lutrampal.controller;

import java.util.List;
import java.util.Map;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public interface RESTDataManager {
    Map<String, Object> getObject(String resource) throws Exception;
    List<Object> getArray(String resource) throws Exception;
    void post(String resource, Map<String, Object> jsonObject);
    void put(String resource, Map<String, Object> jsonObject);
    Map<String, Object> delete(String resource);
}
