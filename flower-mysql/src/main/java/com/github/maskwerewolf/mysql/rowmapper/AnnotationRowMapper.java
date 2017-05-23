package com.github.maskwerewolf.mysql.rowmapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by werewolf on 2017/3/30.
 */
public class AnnotationRowMapper<T> implements RowMapper<T> {


    private Class<T> mappedClass;

    //列名、类型
    private Map<String, Class<?>> mappedColumns;
    //列名、字段名
    private Map<String, String> mappedFields;

    public AnnotationRowMapper(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;

        this.mappedFields = new HashMap();
        this.mappedColumns = new HashMap<>();

//        Field[] fields = mappedClass.getDeclaredFields();

        List<Field> fields = new ArrayList<>(Arrays.asList(mappedClass.getDeclaredFields()));

        Class clzzz = mappedClass;
        while (clzzz != null) {
            clzzz = clzzz.getSuperclass();
            if (clzzz.getName().equals("java.lang.Object")) {
                break;
            }
            fields.addAll(Arrays.asList(clzzz.getDeclaredFields()));
        }

        for (Field field : fields) {
            Class<?> clazz = null;
            String fileName = null;
            String columnName = null;
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columnName = column.name();
            } else {
                columnName = field.getName();
            }

            fileName = field.getName();

            if (field.isAnnotationPresent(Enumerated.class)) {
                Enumerated enumerated = field.getAnnotation(Enumerated.class);
                if (enumerated.value().toString().equals("STRING")) {
                    clazz = String.class;
                } else {
                    clazz = Integer.class;
                }
            } else {
                clazz = field.getType();
            }

            if (null != clazz && null != columnName) {
                mappedColumns.put(columnName, clazz);
                mappedFields.put(columnName, fileName);
            }

        }
    }

    protected Object getColumnValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, requiredType);
    }


    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);

            if (mappedFields.get(column) != null) {
                Object value = getColumnValue(rs, index, mappedColumns.get(column));
                bw.setPropertyValue(mappedFields.get(column), value);
            }

        }
        return mappedObject;
    }
}
