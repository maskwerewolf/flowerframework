package com.github.maskwerewolf.mysql.operation;




import com.github.maskwerewolf.mysql.Jdbc2Template;

import javax.persistence.Table;
import java.util.StringJoiner;


/**
 * Created by werewolf on 2016/11/23.
 */
public abstract class AbstractOperation {

    public static final String space = " ";
    protected Jdbc2Template jdbc2Template;

    protected Class clazz;

    protected String tableName;


    public String tableName() {
        if (tableName != null) {
            return tableName;
        }
        if (this.clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            return table.name();
        } else {
            return clazz.getSimpleName();
        }
    }


    protected abstract StringBuilder sqlPrefix();


    protected static String arrayToString(String[] a) {
        if (a == null || a.length == 0) {
            return " * ";
        }
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String str : a) {
            stringJoiner.add(str);
        }
        return stringJoiner.toString();
    }


}
