package com.github.maskwerewolf.mysql;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/15
 */
public class Select {

    public final List<String> selects = new LinkedList<>();
    public final List<String> columns = new LinkedList<>();

    private Select() {
    }

    public static Select select(String sql) {
        Select select = new Select();
        select.selects.add(sql);
        return select;
    }

    public static Select column(String... column) {
        Select select = new Select();
        select.columns.addAll(Arrays.asList(column));
        return select;
    }

    public Select addSelect(String sql) {
        this.selects.add(sql);
        return this;
    }

    public Select addColumn(String sql) {
        this.columns.add(sql);
        return this;
    }
}
