package com.apps.reflection;

import java.lang.reflect.Field;

/*
 * Merger represents a generic merge operation between two objects of the same type.
 * Null remotes are ignored. no null if primitive are merged into a local.
 * */
public class Merger {

    public <T> T merge(T local, T remote) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = local.getClass();
        Object merged = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            Object localValue = field.get(local);
            Object remoteValue = field.get(remote);

            if (remoteValue == null) {
                field.set(merged, localValue);
            } else if (localValue != null) {
                if(isPrimitive(localValue)) {
                    field.set(merged, remoteValue);
                } else {
                    field.set(merged, this.merge(localValue, remoteValue));
                }
            }
        }
        return (T) merged;
    }

    private boolean isPrimitive (Object localValue) {
        return localValue.getClass().getName().startsWith("java");
    }

}