package com.apps.reflection;
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