package com.github.maskwerewolf.mysql.reflection.invoker;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/16
 */
public interface Invoker {

    Object invoke(Object target, Object[] args);


    Class<?> getType();

}
