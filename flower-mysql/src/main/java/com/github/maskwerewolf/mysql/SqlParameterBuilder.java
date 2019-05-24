package com.github.maskwerewolf.mysql;



import com.github.maskwerewolf.mysql.annotation.Table;
import com.github.maskwerewolf.mysql.reflection.MetaClass;
import com.github.maskwerewolf.mysql.reflection.Reflector;
import com.github.maskwerewolf.mysql.reflection.invoker.Invoker;
import com.github.maskwerewolf.mysql.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/15
 */
public class SqlParameterBuilder {


    private String tableName;
    private String setSql;
    private String whereSql;
    private List<Object> values = new LinkedList<>();
    private String select;
    private Query query;
    private Object object;



    public static SqlParameterBuilder builder(String tableName) {
        SqlParameterBuilder sqlBuilder = new SqlParameterBuilder();
        sqlBuilder.tableName = tableName;
        return sqlBuilder;
    }

    public static SqlParameterBuilder builder(Object object) {
        SqlParameterBuilder sqlBuilder = new SqlParameterBuilder();
        sqlBuilder.object = object;
        return sqlBuilder;
    }

    public static SqlParameterBuilder builder(Class<?> entityClass) {
        SqlParameterBuilder sqlBuilder = new SqlParameterBuilder();
        Table table = entityClass.getAnnotation(Table.class);
        if (table == null) {
            sqlBuilder.tableName = entityClass.getSimpleName();
        } else {
            sqlBuilder.tableName = table.name();
        }
        return sqlBuilder;
    }

    public SqlParameterBuilder select(Select select) {
        this.select = "select * from " + tableName;
        if (!select.selects.isEmpty()) {
            this.select = select.selects.stream().collect(Collectors.joining(" "));
            return this;
        }
        if (!select.columns.isEmpty()) {
            this.select = "select " + select.columns.stream().collect(Collectors.joining(" ,")) + " from " + tableName;
        }
        return this;
    }


    public SqlParameterBuilder set(Update update) {
        StringJoiner set = new StringJoiner(",");
        for (String key : update.criteria.keySet()) {
            if (Update.UpdateCriteria.UpdateType.inc == update.criteria.get(key).getUpdateType()) {
                set.add(" " + key + "= " + key + " + ?");
            } else {
                set.add(" " + key + "= ?");
            }
            values.add(update.criteria.get(key).getValue());
        }
        this.setSql = "set " + set.toString();
        return this;
    }

    public SqlParameterBuilder where(Query query) {
        this.query = query;
        if (query == null) {
            return this;
        }
        Map<String, CriteriaDefinition> criteria = query.criteria;
        if (!query.isEmptyCriteria()) {
            this.whereSql = " where ";
        }
        //==========where========
        for (String key : criteria.keySet()) {
            this.whereSql = this.whereSql + criteria.get(key).getJoin() + key;
            this.values.addAll(Arrays.asList(criteria.get(key).getValues()));
        }
        return this;
    }

    public SqlBuilder update() {
        return new SqlBuilder("update " + this.tableName + " " + this.setSql + this.whereSql, this.values.toArray());
    }

    public SqlBuilder delete() {
        return new SqlBuilder("delete from " + this.tableName + this.whereSql, this.values.toArray());
    }

    public SqlBuilder count() {
        if (StringUtils.isEmpty(this.whereSql)) {
            return new SqlBuilder("select count(1) from " + this.tableName);
        }
        return new SqlBuilder("select count(1) from " + this.tableName + " " + this.whereSql, this.values.toArray());
    }

    public SqlBuilder query() {
        if (StringUtils.isEmpty(this.select)) {
            this.select = "select * from " + tableName;
        }
        if (StringUtils.isEmpty(this.whereSql)) {
            return new SqlBuilder(this.select);
        }

        StringBuffer whereJoin = new StringBuffer(this.whereSql);

        LinkedList<Object> joinValues = new LinkedList<>();
        joinValues.addAll(this.values);
        if (!query.groupBy.isEmpty()) {
            whereJoin.append(" group by ").append(query.groupBy.stream().collect(Collectors.joining(",")));
        }
        if (!query.orderBy.isEmpty()) {
            whereJoin.append(" order by ").append(query.orderBy.stream().collect(Collectors.joining(",")));
        }
        if (!query.page.isEmpty()) {
            whereJoin.append(" limit ?,?");
            joinValues.add(query.page.get("page") - 1);
            joinValues.add(query.page.get("limit"));
        }
        return new SqlBuilder(this.select + whereJoin.toString(), joinValues.toArray());
    }

    public SqlBuilder insert() {
        Reflector reflector = MetaClass.forClass(object.getClass()).getReflector();
        LinkedList<String> columns = new LinkedList();
        columns.addAll(reflector.columns());
        String insert = "insert into " + reflector.tableName() + " (" + columns.stream().collect(Collectors.joining(",")) + ")";
        StringJoiner placeholder = new StringJoiner(",");
        Map<Integer, SqlBuilder.InsertWrapper> insertIndexAndValue = new LinkedHashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            placeholder.add("?");
            Invoker invoker = reflector.getInvokerForColumn(columns.get(i));
            insertIndexAndValue.put(i + 1, new SqlBuilder.InsertWrapper(columns.get(i), invoker, invoker.invoke(object, null)));
        }
        return new SqlBuilder(insert + " values(" + placeholder.toString() + ")", insertIndexAndValue).setReflector(reflector);
    }


    public SqlBuilder insertBatch() {
        return null;
    }

}
