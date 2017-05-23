package com.github.maskwerewolf.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.maskwerewolf.mysql.rowmapper.AnnotationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by werewolf on 2016/11/22.
 */
public class Jdbc2Template {

    private static Logger log = LoggerFactory.getLogger(Jdbc2Template.class);
    private DruidDataSource dataSource;

    public Jdbc2Template(DruidDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *
     * */
    public Long insert(Object obj, String tableName) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(obj);
        return simpleJdbcInsert(tableName).executeAndReturnKey(parameters).longValue();
    }

    public int[] insertBatch(List list, String tableName) {
        SqlParameterSource[] sqlParameterSources = new BeanPropertySqlParameterSource[list.size()];
        for (int i = 0; i < list.size(); i++) {
            sqlParameterSources[i] = (new BeanPropertySqlParameterSource(list.get(i)));
        }
        return simpleJdbcInsert(tableName).executeBatch(sqlParameterSources);
    }


    /**
     * select * from table ......
     */
    public <T> List<T> findSQL(String sql, Class clazz) {
        return namedParameterJdbcTemplate().query(sql, new AnnotationRowMapper<>(clazz));
    }


    /**
     * select * from table where id =:id and name = :name
     */
    public <T> List<T> findSQL(String sql, Class clazz, HashMap hashMap) {
        return namedParameterJdbcTemplate().query(sql, hashMap, new AnnotationRowMapper<>(clazz));
    }


    /**
     * select * from table where id = ? and name  = ?
     */
    public <T> List<T> findSQL(String sql, Class clazz, Object... params) {
        if (log.isInfoEnabled()) {
            log.info("findSQL__:" + sql + "params:" + Arrays.toString(params));
        }
        return jdbcTemplate().query(sql, params, new AnnotationRowMapper<>(clazz));
    }


    /**
     * select * from table ...
     */
    public <T> T findOne(String sql, Class clazz) {
        if (log.isInfoEnabled()) {
            log.info("findSQL__:" + sql);
        }
        return (T) jdbcTemplate().queryForObject(sql, new AnnotationRowMapper<>(clazz));
    }

    public <T> T findOne(String sql, Class clazz, Object... params) {
        try {
            if (log.isInfoEnabled()) {
                log.info("findOne__:" + sql + "params:" + Arrays.toString(params));
            }
            return jdbcTemplate().queryForObject(sql, params, new AnnotationRowMapper<T>(clazz));
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("no result");
            return null;
        }
    }

    public <T> T findOne(String sql, Class clazz, HashMap map) {
        return (T) namedParameterJdbcTemplate().queryForObject(sql, map, new AnnotationRowMapper<>(clazz));
    }


    /**
     * update table set name='111'
     */
    public int update(String sql) {
        return jdbcTemplate().update(sql);
    }

    /**
     * update table set name = ?
     */
    public int update(String sql, Object... params) {
        log.info(sql);
//        for (Object o : params) {
//            System.out.println(o);
//        }
//        return 0;
        return jdbcTemplate().update(sql, params);
    }

    /**
     * update table set name = :name and ....
     */
    public int update(String sql, HashMap map) {
        return namedParameterJdbcTemplate().update(sql, map);
    }


    /**
     * delete from table where name='111'
     */
    public int delete(String sql) {
        return jdbcTemplate().update(sql);
    }

    /**
     * delete from table where  name = ?
     */
    public int delete(String sql, Object... params) {
        log.info("delete --" + sql);
        return jdbcTemplate().update(sql, params);
    }

    /**
     * delete from table where  name = :name and ....
     */
    public int delete(String sql, HashMap map) {
        return namedParameterJdbcTemplate().update(sql, map);
    }


    //=========================================
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    private SimpleJdbcInsert simpleJdbcInsert(String tableName) {
        return new SimpleJdbcInsert(dataSource).withTableName(tableName).usingGeneratedKeyColumns("id");
    }


    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    //=========================================
}
