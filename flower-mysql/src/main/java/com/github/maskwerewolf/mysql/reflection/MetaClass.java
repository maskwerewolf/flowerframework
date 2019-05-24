package com.github.maskwerewolf.mysql.reflection;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/17
 */
public class MetaClass<T> {

    private final Reflector reflector;
    private final Class<T> type;
    private static final ReflectorFactory reflectorFactory;
    static {
        reflectorFactory = new DefaultReflectorFactory();
    }

    private MetaClass(Class<T> type, ReflectorFactory reflectorFactory) {
        this.type = type;
        this.reflector = reflectorFactory.findForClass(type);
    }

    public static MetaClass forClass(Class<?> type) {

        return new MetaClass(type,reflectorFactory);
    }


    public T instance() {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new ReflectionException("instance fail");
    }


    public Reflector getReflector() {
        return reflector;
    }


}
