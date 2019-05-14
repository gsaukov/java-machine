package com.apps.searchandpagination.persistance.query.general;

import javax.persistence.metamodel.SingularAttribute;

public class SearchCriteria<T> {
    private SingularAttribute key;
    private String operation;
    private T value;

    public SearchCriteria(SingularAttribute key, String operation, T value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SingularAttribute getKey() {
        return key;
    }

    public void setKey(SingularAttribute key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
