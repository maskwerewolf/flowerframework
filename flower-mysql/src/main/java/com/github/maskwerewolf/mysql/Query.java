package com.github.maskwerewolf.mysql;

import com.github.maskwerewolf.mysql.utils.StringUtils;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/14
 */
public class Query {

    protected final Map<String, CriteriaDefinition> criteria = new LinkedHashMap<>();
    protected final Map<String, Integer> page = new LinkedHashMap<>();
    protected final List<String> orderBy = new LinkedList();
    protected final List<String> groupBy = new LinkedList();


    protected boolean isEmptyCriteria() {
        return criteria.size() == 0;
    }

    public static Query query() {
        return new Query();
    }

    public static Query query(String where, Object... args) {
        Query query = new Query();
        query.where(where, args);
        return query;
    }

    private Query where(String query, Object... args) {
        this.criteria.put(query, CriteriaDefinition.criteria(args));
        return this;
    }

    public Query and(String query, Object... args) {
        if (criteria.size() >= 1) {
            criteria.put(query, CriteriaDefinition.andCriteria(args));
        } else {
            this.criteria.put(query, CriteriaDefinition.criteria(args));
        }
        return this;
    }

    public Query andIfAbsent(String query, Object arg) {
        if (StringUtils.isNotEmpty(arg)) {
            this.and(query, arg);
        }
        return this;
    }

    public Query or(String query, Object... args) {
        criteria.put("(" + query + ")", CriteriaDefinition.orCriteria(args));
        return this;
    }

    public Query page(int page) {
        this.page.put("page", page);
        return this;
    }

    public Query limit(int limit) {
        this.page.putIfAbsent("page", 1);
        this.page.put("limit", limit);
        return this;
    }

    public Query order(String order) {
        this.orderBy.add(order);
        return this;
    }

    public Query group(String group) {
        this.groupBy.add(group);
        return this;
    }


}
