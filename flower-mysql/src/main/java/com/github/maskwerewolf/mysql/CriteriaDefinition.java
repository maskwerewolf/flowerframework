package com.github.maskwerewolf.mysql;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/15
 */
class CriteriaDefinition {

    private Object[] values;
    private String join;

    public static CriteriaDefinition criteria(Object[] values) {
        return new CriteriaDefinition("", values);
    }

    public static CriteriaDefinition andCriteria(Object[] values) {
        return new CriteriaDefinition(" and ", values);
    }

    public static CriteriaDefinition orCriteria(Object[] values) {
        return new CriteriaDefinition(" or ", values);
    }

    private CriteriaDefinition(String join, Object[] values) {
        this.values =  values;
        this.join = join;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }
}
