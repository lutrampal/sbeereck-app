package com.sbeereck.lutrampal.controller;

import java.util.List;
import java.util.Map;

/**
 * Created by lutrampal on 02/01/18 for S'Beer Eck.
 */

public interface RESTDataManager {
    Map<String, Object> getObject(String resource) throws Exception;
    List<Object> getArray(String resource) throws Exception;
    Map<String, Object> post(String resource, Object jsonObject) throws Exception;
    Map<String, Object> put(String resource, Object jsonObject) throws Exception;
    Map<String, Object> delete(String resource) throws Exception;
}
