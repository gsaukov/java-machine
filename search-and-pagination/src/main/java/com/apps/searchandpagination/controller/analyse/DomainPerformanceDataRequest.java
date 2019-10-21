package com.apps.searchandpagination.controller.analyse;

public class DomainPerformanceDataRequest {
    private String domain;
    private Integer size;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
