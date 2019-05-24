package com.github.maskwerewolf.mysql;

import com.github.maskwerewolf.mysql.reflection.Reflector;
import com.github.maskwerewolf.mysql.reflection.invoker.Invoker;


import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/15
 */
public class SqlBuilder {


    public static class InsertWrapper {

        private String column;
        private transient Invoker invoker;
        private Object value;

        public static InsertWrapper build(String column, Invoker invoker, Object value) {
            return new InsertWrapper(column, invoker, value);
        }

        public InsertWrapper(String column, Invoker invoker, Object value) {
            this.column = column;
            this.invoker = invoker;
            this.value = value;
        }

        public String getColumn() {
            return column;
        }

        public Invoker getInvoker() {
            return invoker;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "{" + column + " = " + value + "}";
        }
    }

    private String sql;
    private Map<Integer, InsertWrapper> insertIndexAndValue = new LinkedHashMap<>();
    private Reflector reflector;
    private Object[] values;


    public SqlBuilder(String sql) {
        this.sql = sql;
        this.values = null;
    }

    public SqlBuilder(String sql, Object[] values) {
        this.sql = sql;
        this.values = values;
    }

    public SqlBuilder(String sql, Map<Integer, InsertWrapper> insertIndexAndValue) {
        this.sql = sql;
        this.insertIndexAndValue = insertIndexAndValue;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public Map<Integer, InsertWrapper> getInsertIndexAndValue() {
        return insertIndexAndValue;
    }

    @Override
    public String toString() {
        if (values == null) {
            List<String> objectList = new ArrayList<>();
            for (Integer index : insertIndexAndValue.keySet()) {
                objectList.add(index + "," + insertIndexAndValue.get(index).toString());
            }
            this.values = objectList.toArray();
        }
        return "{" + sql + " | " + Arrays.toString(values) + "}";
    }

    public Reflector getReflector() {
        return reflector;
    }

    public SqlBuilder setReflector(Reflector reflector) {
        this.reflector = reflector;
        return this;
    }
}
