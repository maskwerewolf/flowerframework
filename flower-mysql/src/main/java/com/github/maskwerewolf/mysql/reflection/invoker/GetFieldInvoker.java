package com.github.maskwerewolf.mysql.reflection.invoker;



import com.github.maskwerewolf.mysql.reflection.ReflectionException;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/16
 */
public class GetFieldInvoker implements Invoker {

    private final Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }


    @Override
    public Object invoke(Object target, Object[] args) {
        try {
            return this.field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw  new ReflectionException("invoker get failed");
    }


    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
