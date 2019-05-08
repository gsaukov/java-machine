package com.apps.searchandpagination.persistance.query;

import javax.persistence.metamodel.SingularAttribute;

public class SearchCriteria {
    private SingularAttribute key;
    private String operation;
    private Object value;

    public SearchCriteria(SingularAttribute key, String operation, Object value) {
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
