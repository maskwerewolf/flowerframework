package com.github.maskwerewolf.mysql.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.maskwerewolf.mysql.Jdbc2Template;
import com.github.maskwerewolf.mysql.query.QuerySQL;
import com.github.maskwerewolf.mysql.query.WhereBuild;


import java.util.List;
import java.util.Map;


/**
 * Created by werewolf on 2016/11/21.
 */

public class Query extends AbstractOperation implements QuerySQL {

    public static final String SELECT = "select ";
    public static final String FROM = " from ";
    private String[] columns;
    private WhereBuild whereBuild;


    public Query(DruidDataSource dataSource, String tableName, Class clazz, String... columns) {
        this.columns = columns;
        this.jdbc2Template = new Jdbc2Template(dataSource);
        this.clazz = clazz;
        this.tableName = tableName;
        whereBuild = new WhereBuild();
    }


    public static Query query(String tableName, DruidDataSource dataSource, Class clazz) {
        return new Query(dataSource, tableName, clazz);
    }


    public static Query query(Class clazz, String tableName, DruidDataSource dataSource, String... columns) {
        return new Query(dataSource, tableName, clazz, columns);
    }


    public Query where(String expression, Object... values) {
        whereBuild.where(expression, values);
        return this;
    }

    public Query order(String expression) {
        whereBuild.order(expression);
        return this;
    }

    @Override
    public <T> List<T> page(int limit, int page) {
        return this.jdbc2Template.findSQL(
                sqlPrefix().append(whereBuild.page(limit, page).buildSuffix()).toString(),
                this.clazz,
                this.whereBuild.whereValues.toArray()
        );
    }

    @Override
    public <T> List<T> all() {
        return this.jdbc2Template.findSQL(
                sqlPrefix().append(whereBuild.buildSuffix()).toString(),
                this.clazz,
                this.whereBuild.whereValues.toArray()
        );
    }

    @Override
    public <T> List<T> limit(int size) {
        return this.jdbc2Template.findSQL(
                sqlPrefix().append(whereBuild.limit(size).buildSuffix()).toString(),
                this.clazz,
                this.whereBuild.whereValues.toArray()
        );
    }

    @Override
    public <T> T first() {
        return this.jdbc2Template.findOne(
                sqlPrefix().append(whereBuild.limit(1).buildSuffix()).toString(),
                this.clazz,
                this.whereBuild.whereValues.toArray()
        );

    }

    @Override
    public <T> T last() {
        return this.jdbc2Template.findOne(
                sqlPrefix().append(whereBuild.order("id desc").limit(1).buildSuffix()).toString(),
                this.clazz,
                this.whereBuild.whereValues.toArray()
        );
    }

    @Override
    public <T> T find(Object id) {
        return this.jdbc2Template.findOne(
                sqlPrefix().append(whereBuild.where("id=?", id).buildSuffix()).toString(),
                this.clazz,
                this.whereBuild.whereValues.toArray()
        );
    }


    @Override
    public Long count() {
        return this.jdbc2Template.jdbcTemplate().queryForObject(
                sqlPrefixCount().append(whereBuild.buildSuffix()).toString(),
                Long.class,
                this.whereBuild.whereValues.toArray()
        );
    }


    @Override
    public Map findOfMap() {
        return this.jdbc2Template.jdbcTemplate().queryForMap(
                sqlPrefix().append(whereBuild.buildSuffix()).toString(),
                this.whereBuild.whereValues.toArray()
        );
    }


    @Override
    public List<Map<String, Object>> pageOfMap(int limit, int page) {
        return this.jdbc2Template.jdbcTemplate().queryForList(sqlPrefix().append(whereBuild.page(limit, page).buildSuffix()).toString(),
                this.whereBuild.whereValues.toArray()
        );
    }


    private StringBuilder sqlPrefixCount() {
        StringBuilder count = new StringBuilder(SELECT);
        count.append(" count(1) ");
        count.append(FROM);
        count.append(this.tableName());
        return count;
    }

    @Override
    protected StringBuilder sqlPrefix() {
        StringBuilder prefix = new StringBuilder(SELECT);
        prefix.append(arrayToString(columns));
        prefix.append(FROM);
        prefix.append(this.tableName());
        return prefix;
    }


}
