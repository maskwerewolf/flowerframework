package com.github.maskwerewolf.mysql.reflection.invoker;


import com.github.maskwerewolf.mysql.reflection.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MethodInvoker implements Invoker {

    private final Class<?> type;
    private final Method method;

    public MethodInvoker(Method method) {
        this.method = method;

        if (method.getParameterTypes().length == 1) {
            type = method.getParameterTypes()[0];
        } else {
            type = method.getReturnType();
        }
    }


    @Override
    public Object invoke(Object target, Object[] args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw  new ReflectionException("invoker  failed");
    }



    @Override
    public Class<?> getType() {
        return type;
    }
}
