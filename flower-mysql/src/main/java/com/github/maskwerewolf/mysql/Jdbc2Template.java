package com.github.maskwerewolf.mysql;

import com.github.maskwerewolf.mysql.utils.Optional2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/14
 */
public class Jdbc2Template {

    private static final Logger logger = LoggerFactory.getLogger(Jdbc2Template.class);
    private final JdbcTemplate jdbcTemplate;

    public Jdbc2Template(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Number insert(Object object) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(object).insert();
        String primaryKey = sqlBuilder.getReflector().getPrimaryKey();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlBuilder.getSql(), new String[]{primaryKey});
            prepareStatement(ps, sqlBuilder);
            return ps;
        }, keyHolder);
        return keyHolder.getKey();
    }

    public int insertBatch(List<?> objectList) {

        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(objectList.get(0)).insert();
        return jdbcTemplate.batchUpdate(sqlBuilder.getSql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) {
                SqlBuilder _sqlBuilder = SqlParameterBuilder.builder(objectList.get(i)).insert();
                prepareStatement(ps, _sqlBuilder);
            }

            @Override
            public int getBatchSize() {
                return objectList.size();
            }
        }).length;
    }

    private void prepareStatement(PreparedStatement ps, SqlBuilder sqlBuilder) {
        sqlBuilder.getInsertIndexAndValue().forEach((k, v) -> {
            try {
                if (v.getInvoker().getType().isEnum()) {
                    ps.setString(k, v.getValue().toString());
                } else {
                    ps.setObject(k, v.getValue());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    public int update(Query query, Update update, Class<?> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).set(update).where(query).update();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.update(sqlBuilder.getSql(), sqlBuilder.getValues());
    }


    public int update(Query query, Update update, String tableName) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(tableName).set(update).where(query).update();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.update(sqlBuilder.getSql(), sqlBuilder.getValues());
    }


    public <T> List<T> find(Query query, Class<T> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.query(sqlBuilder.getSql(), new AnnotationRowMapper<T>(entityClass), sqlBuilder.getValues());
    }

    public <T> List<T> find(Select select, Query query, Class<T> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).select(select).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.query(sqlBuilder.getSql(), new AnnotationRowMapper<T>(entityClass), sqlBuilder.getValues());
    }

    public List<Map<String, Object>> findForMaps(Query query, Class<?> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return jdbcTemplate.query(sqlBuilder.getSql(), new ColumnMapRowMapper(), sqlBuilder.getValues());
    }

    public List<Map<String, Object>> findForMaps(Select select, Query query, Class<?> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).select(select).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return jdbcTemplate.query(sqlBuilder.getSql(), new ColumnMapRowMapper(), sqlBuilder.getValues());
    }


    public <T> T findOne(Query query, Class<T> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), sqlBuilder.getValues(), new AnnotationRowMapper<T>(entityClass));
    }

    public Map<String, Object> findForMap(Query query, Class<?> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), sqlBuilder.getValues(), new ColumnMapRowMapper());
    }

    public Map<String, Object> findForMap(Query query, String tableName) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(tableName).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), sqlBuilder.getValues(), new ColumnMapRowMapper());
    }

    public <T> T findOne(Select select, Query query, Class<T> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).select(select).where(query).query();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), sqlBuilder.getValues(), new AnnotationRowMapper<>(entityClass));
    }

    public int delete(Query query, Class<?> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).where(query).delete();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.update(sqlBuilder.getSql(), sqlBuilder.getValues());
    }


    public int delete(Query query, String tableName) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(tableName).where(query).delete();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.update(sqlBuilder.getSql(), sqlBuilder.getValues());
    }

    public long count(Query query, String tableName) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(tableName).where(query).count();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), Long.class, sqlBuilder.getValues());
    }


    public long count(Query query, Class<?> entityClass) {
        SqlBuilder sqlBuilder = SqlParameterBuilder.builder(entityClass).where(query).count();
        Optional2.ofNullable(logger.isDebugEnabled()).isPresentTrue(v -> logger.debug(sqlBuilder.toString()));
        return this.jdbcTemplate.queryForObject(sqlBuilder.getSql(), Long.class, sqlBuilder.getValues());
    }
}
