package org.tinywind.paypalexpresscheckout.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tinywind on 2016-07-16.
 */
public class ReflectionUtil {
    public static Object getValue(Object obj, Class<?> klass, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object value;
        try {
            value = field.get(obj);
        } catch (Exception ignored) {
            String fieldName = field.getName();
            Method getter = klass.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            value = getter.invoke(obj);
        }
        return value;
    }

    public static void setValue(Object obj, Class<?> klass, Field field, Object value) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        try {
            field.set(obj, field.getType().cast(value));
        } catch (IllegalAccessException ignored) {
            String fieldName = field.getName();
            Method setter = klass.getDeclaredMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            setter.invoke(obj, field.getType().cast(value));
        }
    }
}
