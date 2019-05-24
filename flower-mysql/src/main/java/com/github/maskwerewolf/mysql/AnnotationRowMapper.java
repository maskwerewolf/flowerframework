package com.github.maskwerewolf.mysql;

import com.github.maskwerewolf.mysql.reflection.MetaClass;
import com.github.maskwerewolf.mysql.reflection.invoker.Invoker;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/15
 */
public class AnnotationRowMapper<T> implements RowMapper<T> {

    private final MetaClass<T> metaClass;

    public AnnotationRowMapper(Class<T> entityClass) {
        this.metaClass = MetaClass.forClass(entityClass);
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T mapper = this.metaClass.instance();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i = 1; i < columnCount + 1; i++) {
            if (metaClass.getReflector().setInvokerForColumn(rsmd.getColumnLabel(i)).getType().isEnum()) {
                Invoker invoker = metaClass.getReflector().setInvokerForColumn(rsmd.getColumnLabel(i));
                invoker.invoke(mapper, new Object[]{Enum.valueOf((Class<Enum>) invoker.getType(), rs.getString(i))});
            } else {
                metaClass.getReflector().setInvokerForColumn(rsmd.getColumnLabel(i)).invoke(mapper, new Object[]{rs.getObject(i)});
            }

        }
        return mapper;
    }


}

