package com.github.maskwerewolf.mysql.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.maskwerewolf.mysql.Jdbc2Template;
import com.github.maskwerewolf.mysql.query.WhereBuild;


/**
 * Created by werewolf on 2016/11/23.
 */
public class Delete extends AbstractOperation {

    public static final String DELETE = "delete ";
    public static final String FROM = " from ";

    private WhereBuild whereBuild;

    public Delete where(String expression, Object... values) {
        whereBuild.where(expression, values);
        return this;
    }

    public Delete(Class clazz, String tableName, DruidDataSource dataSource) {
        this.clazz = clazz;
        this.jdbc2Template = new Jdbc2Template(dataSource);
        this.tableName = tableName;
        whereBuild = new WhereBuild();
    }

    public static Delete delete(Class clazz, String tableName, DruidDataSource dataSource) {
        return new Delete(clazz, tableName, dataSource);
    }


    public int delete() {
        if (this.whereBuild.whereValues == null || this.whereBuild.whereValues.size() == 0) {
            return 0;
        }
        return this.jdbc2Template.delete(sqlPrefix().append(this.whereBuild.buildSuffix()).toString(), this.whereBuild.whereValues.toArray());
    }

    public int delete(Object id) {
        whereBuild.where("id = ? ", id);
        return this.delete();
    }


    @Override
    protected StringBuilder sqlPrefix() {
        StringBuilder stringBuilder = new StringBuilder(DELETE);
        stringBuilder.append(FROM);
        stringBuilder.append(this.tableName());
        return stringBuilder;
    }
}
