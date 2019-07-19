package com.apps.searchandpagination.service.account;

public class AccountPerformance {
    private String year;
    private Integer assets;

    public AccountPerformance(String year, Integer assets) {
        this.year = year;
        this.assets = assets;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getAssets() {
        return assets;
    }

    public void setAssets(Integer assets) {
        this.assets = assets;
    }
}
