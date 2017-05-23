package com.github.maskwerewolf.mysql.query;


import java.util.LinkedList;

/**
 * Created by werewolf on 2016/11/23.
 */
public class WhereBuild {

    public static enum Up {
        OR(" or "), AND(" and "), EMPTY(" ");
        private String value;

        Up(String value) {
            this.value = value;
        }
    }

    public static final String space = " ";

    private LinkedList<String> whereExpressions = new LinkedList<>();
    public LinkedList<Object> whereValues = new LinkedList<>();

    private StringBuilder orderExpressions = null;
    private StringBuilder limitExpressions = null;
    private StringBuilder pageExpressions = null;


    public WhereBuild where(String expression, Object... value) {
        whereExpressions.add(expression);
        for (Object v : value) {
            whereValues.addLast(v);
        }
        return this;
    }

    public WhereBuild order(String expression) {
        orderExpressions = new StringBuilder("order by").append(space).append(expression);
        return this;
    }

    public WhereBuild page(int limit, int page) {
        pageExpressions = new StringBuilder("limit").append(space).append((page - 1) * limit).append(",").append(limit);
        return this;
    }

    public WhereBuild limit(int size) {
        limitExpressions = new StringBuilder("limit").append(space).append(size);
        return this;
    }

    public StringBuilder buildSuffix() {
        StringBuilder sql = new StringBuilder();
        if (whereExpressions != null && whereExpressions.size() > 0) {
            sql.append(space).append("where").append(space);
            whereExpressions.forEach(v -> sql.append(v).append(space).append("and").append(space));
            sql.delete(sql.length() - 4, sql.length());
        }

        if (orderExpressions != null && orderExpressions.length() > 0) {
            sql.append(space).append(orderExpressions);
        }
        if (limitExpressions != null && limitExpressions.length() > 0) {
            sql.append(space).append(limitExpressions);
        }

        if (pageExpressions != null && pageExpressions.length() > 0) {
            sql.append(space).append(pageExpressions);
        }
        return sql;
    }

}
