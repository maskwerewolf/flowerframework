package com.github.maskwerewolf.mysql.reflection.invoker;

import java.lang.reflect.Field;


public class SetFieldInvoker implements Invoker {
    private final Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }


    @Override
    public Object invoke(Object target, Object[] args) {
        try {
            field.set(target, args[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
