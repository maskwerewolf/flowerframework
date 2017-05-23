package com.github.maskwerewolf.mysql.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.maskwerewolf.mysql.Jdbc2Template;
import com.github.maskwerewolf.mysql.query.WhereBuild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by werewolf on 2016/11/23.
 */
public class Update extends AbstractOperation {

    public static final String UPDATE = "update ";
    public static final String SET = " set ";
    private static Logger log = LoggerFactory.getLogger("Update");

    private WhereBuild whereBuild;
    private Map<String, Object> incMap;

    public Update where(String expression, Object... values) {
        whereBuild.where(expression, values);
        return this;
    }

    public Update(Class clazz, String tableName, DruidDataSource dataSource) {
        this.clazz = clazz;
        this.jdbc2Template = new Jdbc2Template(dataSource);
        this.tableName = tableName;
        this.incMap = new HashMap<>();
        whereBuild = new WhereBuild();
    }


    public static Update update(Class clazz, String tableName, DruidDataSource dataSource) {

        return new Update(clazz, tableName, dataSource);
    }

    public Update inc(String column, Object value) {
        incMap.put(column, value);
        return this;
    }

    public int set() {

        if (incMap.size() == 0) {
            return 0;
        }

        return jdbc2Template.update(sqlPrefix() + incSql().toString() + this.whereBuild.buildSuffix(), this.whereBuild.whereValues.toArray());
    }

    public int set(String column, Object value) {

        this.whereBuild.whereValues.addFirst(value);
        StringBuilder updateColumn = new StringBuilder();

        updateColumn.append(column).append(" = ").append("?");
        if (incSql().length() > 0) {
            updateColumn.append(",").append(incSql());
        }

        return jdbc2Template.update(sqlPrefix() + updateColumn.toString() + this.whereBuild.buildSuffix(), this.whereBuild.whereValues.toArray());
    }


    public int set(Map<String, Object> map) {
        LinkedList<Object> setValues = new LinkedList();

        StringJoiner stringJoiner = new StringJoiner(" = ?, ", "", " = ?");
        map.forEach((key, value) -> {
            stringJoiner.add(key);
            setValues.addLast(value);
        });

        StringBuilder column = new StringBuilder().append(stringJoiner);
        if (incSql().length() > 0) {
            column.append(",").append(incSql());
        }
        this.whereBuild.whereValues.forEach(v -> setValues.addLast(v));
        return jdbc2Template.update(sqlPrefix().append(column).append(whereBuild.buildSuffix()).toString(),
                setValues.toArray()
        );
    }


    @Override
    protected StringBuilder sqlPrefix() {
        StringBuilder stringBuilder = new StringBuilder(UPDATE);
        stringBuilder.append(this.tableName());
        stringBuilder.append(this.SET);
        return stringBuilder;
    }

    private StringBuilder incSql() {
        StringBuilder updateColumn = new StringBuilder();
        if (incMap.size() > 0) {
            incMap.forEach((k, v) -> {
                updateColumn.append(k + " = ").append(k);
                updateColumn.append(" + ").append(v).append(space);
                updateColumn.append(",");
            });
            int len = updateColumn.length();
            updateColumn.delete(len - 1, len);
        }
        return updateColumn;
    }
}
