package com.github.maskwerewolf.mysql.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by werewolf on 2017/3/30.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Sharding {
    String column();

    Type type();

    String prefix();

    enum Type {
        YEAR,
        QUARTER,
        MONTH;
    }
}
