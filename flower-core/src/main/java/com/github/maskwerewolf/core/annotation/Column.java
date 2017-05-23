package com.github.maskwerewolf.core.annotation;


/**
 * Created by werewolf on 2016/11/18.
 */
@Deprecated
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Column {
    String name();
}
