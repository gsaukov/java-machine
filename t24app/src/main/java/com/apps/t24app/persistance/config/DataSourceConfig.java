package com.apps.t24app.persistance.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "someconfig")
public class DataSourceConfig {

    private String url;
    private String username;
    private String password;
    private Class type;
    private String driverClassName;

//    @Bean
//    public DataSource dataSource() {
//
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

}
