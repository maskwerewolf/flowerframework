package com.github.maskwerewolf.mysql;

import com.github.maskwerewolf.mysql.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/14
 */
public class Update {

    protected final Map<String, UpdateCriteria> criteria = new LinkedHashMap<>();

    public static Update update(String column, Object value) {
        return new Update().set(column, value);
    }


    public Update set(String column, Object value) {
        criteria.put(column, UpdateCriteria.setUpdate(value));
        return this;
    }

    public Update setIfAbsent(String column, Object value) {
        if (StringUtils.isNotEmpty(value)) {
            criteria.put(column, UpdateCriteria.setUpdate(value));
        }
        return this;
    }

    public Update inc(String column, Object value) {
        criteria.put(column, UpdateCriteria.incUpdate(value));
        return this;
    }

    public Update incIfAbsent(String column, Object value) {
        if (StringUtils.isNotEmpty(value)) {
            criteria.put(column, UpdateCriteria.incUpdate(value));
        }
        return this;
    }

    public static class UpdateCriteria {
        public enum UpdateType {
            set, inc
        }

        private Object value;
        private UpdateType updateType;


        public static UpdateCriteria setUpdate(Object value) {
            return new UpdateCriteria(UpdateType.set, value);
        }

        public static UpdateCriteria incUpdate(Object value) {
            return new UpdateCriteria(UpdateType.inc, value);
        }

        public UpdateCriteria(UpdateType updateType, Object value) {
            this.value = value;
            this.updateType = updateType;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public UpdateType getUpdateType() {
            return updateType;
        }

        public void setUpdateType(UpdateType updateType) {
            this.updateType = updateType;
        }
    }
}
