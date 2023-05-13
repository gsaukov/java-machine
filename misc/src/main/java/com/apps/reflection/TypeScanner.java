package com.apps.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeScanner {


    /*
    * Scanner used to build reference map with links to targetClass in scanned clazz;
    */
    public static Map<String, Object> scan(Class targetClass, Class clazz) {
        TypeScanner scanner = new TypeScanner(targetClass);
        return scanner.scanClass(clazz.getSimpleName(), clazz, new HashSet<>());
    }

    public static Map<String, Object> toFlatMap(Map<String, Object> map) {
        Map<String, Object> flattenedMap = flatten(map)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return flattenedMap;
    }

    private static Stream<Map.Entry<String, Object>> flatten(Map<String, Object> map) {
        return map.entrySet()
                .stream()
                .flatMap(TypeScanner::extractValue);
    }

    private static Stream<Map.Entry<String, Object>> extractValue(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof Map) {
            return flatten((Map<String, Object>) entry.getValue());
        } else {
            return Stream.of(entry);
        }
    }

    private Class targetClass;

    private TypeScanner(final Class targetClass) {
        this.targetClass = targetClass;
    }

    private Map<String, Object> scanClass(String parentName, Class clazz, Set<Class> visited) {
        Map<String, Object> res = new HashMap<>();

        if (targetClass.isAssignableFrom(clazz)) {
            res.put(parentName, clazz);
            return res;
        } else if (visited.contains(clazz)) {
            return res;
        }

        visited.add(clazz);

        for (Field field : getFields(clazz)) {
            Class<?> type = field.getType();
            String name = parentName + "." + field.getName()+ "(" + type.getSimpleName() + ")";
            if (type.isEnum() || type.isPrimitive()) {
                continue;
            } else if (Iterable.class.isAssignableFrom(type)) {
                Map<String, Object> scanRes = scanList(name, field.getGenericType(), new HashSet<>(visited));
                if (!scanRes.isEmpty()) {
                    res.put(name, scanRes);
                }
            } else if (Map.class.isAssignableFrom(type)) {
                Map<String, Object> scanRes = scanMap(name, field.getGenericType(), new HashSet<>(visited));
                if (!scanRes.isEmpty()) {
                    res.put(name, scanRes);
                }
            } else {
                Map<String, Object> scanRes = scanClass(name, type, new HashSet<>(visited));
                if (!scanRes.isEmpty()) {
                    res.put(name, scanRes);
                }
            }
        }
        return res;
    }

    private Map<String, Object> scanList(String parentName, Type genericType, Set<Class> visited) {
        ParameterizedType listType = getParameterizedType(genericType);
        if (listType != null) {
            Type value = listType.getActualTypeArguments()[0];
            if (value instanceof Class) {//could be sun.reflect.generics.reflectiveObjects.TypeVariableImpl which is not Class
                return scanClass(parentName, (Class) value, visited);
            }
        }
        return Collections.emptyMap();
    }

    private Map<String, Object> scanMap(String parentName, Type genericType, Set<Class> visited) {
        ParameterizedType mapTypes = getParameterizedType(genericType);
        if (mapTypes != null) {
            Type key = mapTypes.getActualTypeArguments()[0];
            Type value = mapTypes.getActualTypeArguments()[1];
            if (value instanceof Class) { //could be sun.reflect.generics.reflectiveObjects.TypeVariableImpl which is not Class
                return scanClass(parentName, (Class) value, visited);
            }
        }
        //Nothing found fallback.
        return Collections.emptyMap();
    }

    private ParameterizedType getParameterizedType(Type c) { //Support of legacy collections Hashtable
        while (c != null && c != Object.class) {
            if (ParameterizedType.class.isAssignableFrom(c.getClass())) {
                return (ParameterizedType) c;
            }
            c = ((Class) c).getGenericSuperclass();
        }
        return null;
    }

    private List<Field> getFields(Class c) {
        List<Field> fields = new ArrayList<>();
        while (c != null && c != Object.class) {//for interfaces super class is null.
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return fields;
    }

}
