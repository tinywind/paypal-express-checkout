package org.tinywind.paypalexpresscheckout.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    public static void setValue(Object obj, Class<?> klass, Field field, Object value) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        try {
            field.set(obj, field.getType().cast(value));
        } catch (Exception ignored) {
            String fieldName = field.getName();
            Method setter = klass.getDeclaredMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
            try {
                setter.invoke(obj, field.getType().cast(value));
            } catch (Exception ignored1) {
                if (field.getType().isEnum()) {
                    try {
                        value = field.getType().getMethod("valueOf", String.class).invoke(null, value);
                    } catch (Exception ignored2) {
                        value = field.getType().getMethod("stringOf", String.class).invoke(null, value);
                    }
                    try {
                        field.set(obj, field.getType().cast(value));
                    } catch (Exception ignored2) {
                        setter.invoke(obj, field.getType().cast(value));
                    }
                } else if (field.getType() == String.class) {
                    final Constructor<?> constructor = field.getType().getConstructor(String.class);
                    setter.invoke(obj, constructor.newInstance(value));
                }
            }
        }
    }
}
