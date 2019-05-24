package com.github.maskwerewolf.mysql.reflection;

import com.github.maskwerewolf.mysql.annotation.Column;
import com.github.maskwerewolf.mysql.annotation.Id;
import com.github.maskwerewolf.mysql.annotation.Table;
import com.github.maskwerewolf.mysql.reflection.invoker.GetFieldInvoker;
import com.github.maskwerewolf.mysql.reflection.invoker.Invoker;
import com.github.maskwerewolf.mysql.reflection.invoker.MethodInvoker;
import com.github.maskwerewolf.mysql.reflection.invoker.SetFieldInvoker;
import com.github.maskwerewolf.mysql.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ReflectPermission;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/16
 */
public class Reflector {
    private static final Logger logger = LoggerFactory.getLogger(Reflector.class);

    private final Class<?> clazz;
    private final Map<String, Invoker> setMethods = new HashMap<>();
    private final Map<String, Invoker> getMethods = new HashMap<>();
    private final Map<String, String> propertyColumn = new HashMap<>();
    private final Map<String, String> columnProperty = new HashMap<>();
    private String primaryKey = "id";


    public Reflector(Class<?> clazz) {
        this.clazz = clazz;
        addGetMethods(clazz);
        addSetMethods(clazz);
        addFields(clazz);
    }

    private void addGetMethods(Class<?> cls) {
        Method[] methods = getClassMethods(cls);
        for (Method method : methods) {
            if (method.getParameterTypes().length > 0) {
                continue;
            }
            String name = method.getName();
            if ((name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2)) {
                name = PropertyNamer.methodToProperty(name);
                addGetMethod(name, method);
            }
        }
    }

    private void addGetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            getMethods.put(name, new MethodInvoker(method));
        }
    }

    private void addSetMethods(Class<?> cls) {
        Method[] methods = getClassMethods(cls);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3) {
                if (method.getParameterTypes().length == 1) {
                    name = PropertyNamer.methodToProperty(name);
                    addSetMethod(name, method);
                }
            }
        }
    }

    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (canControlMemberAccessible()) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                }
            }
            if (field.isAccessible()) {
                int modifiers = field.getModifiers();
                if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                    propertyColumn.put(field.getName(), getColumnName(field));
                    columnProperty.put(getColumnName(field), field.getName());
                }
                if (!setMethods.containsKey(field.getName())) {
                    if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                        addSetField(field);
                    }
                }
                if (!getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            addFields(clazz.getSuperclass());
        }
    }

    private void addSetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            setMethods.put(field.getName(), new SetFieldInvoker(field));
        }
    }

    private void addGetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            getMethods.put(field.getName(), new GetFieldInvoker(field));
        }
    }

    public Set<String> columns() {
        return columnProperty.keySet();
    }

    private void addSetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            setMethods.put(name, new MethodInvoker(method));
        }
    }


    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    private String getColumnName(Field field) {
        String name = "";
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            name = column.name();
        } else if (field.isAnnotationPresent(Id.class)) {
            Id id = field.getAnnotation(Id.class);
            this.primaryKey = StringUtils.isNotEmpty(id.name()) ? id.name() : field.getName();
            name = id.name();
        }
        if (StringUtils.isEmpty(name)) {
            return field.getName();
        }
        return name;
    }

    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<>();
        Class<?> currentClass = cls;
        while (currentClass != null && currentClass != Object.class) {
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
            currentClass = currentClass.getSuperclass();
        }

        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[methods.size()]);
    }


    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                String signature = getSignature(currentMethod);
                if (!uniqueMethods.containsKey(signature)) {
                    if (canControlMemberAccessible()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                            // Ignored. This is only a final precaution, nothing we can do.
                        }
                    }

                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    public String tableName() {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            return clazz.getSimpleName();
        } else {
            return table.name();
        }

    }

    public static boolean canControlMemberAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }


    public Invoker setInvoker(String propertyName) {
        Invoker method = setMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + clazz + "'");
        }
        return method;
    }

    public Invoker setInvokerForColumn(String columnName) {

        Invoker method = setMethods.get(columnProperty.get(columnName));
        if (method == null) {
            throw new ReflectionException("There is no setter for property named '" + columnProperty.get(columnName) + "' in '" + clazz + "'");
        }
        return method;
    }

    public Invoker getInvoker(String propertyName) {
        Invoker method = getMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + clazz + "'");
        }
        return method;
    }

    public Invoker getInvokerForColumn(String columnName) {
        Invoker method = getMethods.get(columnProperty.get(columnName));
        if (method == null) {
            throw new ReflectionException("There is no getter for property named '" + columnName + "' in '" + clazz + "'");
        }
        return method;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

//

}
