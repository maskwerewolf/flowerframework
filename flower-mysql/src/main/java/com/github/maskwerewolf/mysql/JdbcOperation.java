package com.github.maskwerewolf.mysql;


import com.alibaba.druid.pool.DruidDataSource;
import com.github.maskwerewolf.mysql.operation.Delete;
import com.github.maskwerewolf.mysql.operation.Insert;
import com.github.maskwerewolf.mysql.operation.Query;
import com.github.maskwerewolf.mysql.operation.Update;

import java.util.List;

/**
 * Created by werewolf on 2016/11/23.
 */
public class JdbcOperation {


    private DruidDataSource defaultDataSource;

    private String tableName;


    public JdbcOperation(DruidDataSource dataSource) {
        this.defaultDataSource = dataSource;
    }

    public JdbcOperation(DruidDataSource dataSource, String tableName) {
        this.defaultDataSource = dataSource;
        this.tableName = tableName;
    }


    public Jdbc2Template SQL() {
        return new Jdbc2Template(this.defaultDataSource);
    }

    //==========================
    public Long insert(Object object) {
        return Insert.insert(this.defaultDataSource, object, this.tableName);
    }


    public int[] insertBatch(List list) {
        return Insert.insertBatch(this.defaultDataSource, list, this.tableName);
    }

    public Query query(Class clazz, String... columns) {
        return Query.query(clazz, this.tableName, this.defaultDataSource, columns);
    }

    public Delete delete(Class clazz) {
        return Delete.delete(clazz, this.tableName, this.defaultDataSource);
    }

    public Update update(Class clazz) {
        return Update.update(clazz, this.tableName, this.defaultDataSource);
    }


}
