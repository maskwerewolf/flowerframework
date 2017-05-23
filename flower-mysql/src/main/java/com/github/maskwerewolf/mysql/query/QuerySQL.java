package com.github.maskwerewolf.mysql.query;

import java.util.List;
import java.util.Map;

/**
 * Created by werewolf on 2016/11/23.
 */
public interface QuerySQL {

    <T> List<T> page(int limit, int page);

    <T> List<T> all();

    <T> List<T> limit(int size);

    <T> T first();

    <T> T last();

    <T> T find(Object id);


    Long count();


    Map findOfMap();

    List<Map<String, Object>> pageOfMap(int limit, int page);


}
