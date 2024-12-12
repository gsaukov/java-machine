package com.apps.searchandpagination.cassandra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.host}")
    private String cassandraHost;

    @Value("${cassandra.port}")
    private int cassandraPort;

    @Override
    protected String getKeyspaceName() {
        return "geoKeySpace";
    }

    @Bean
    public CqlSessionFactoryBean cassandraSession() {
        CqlSessionFactoryBean bean = super.cassandraSession();
        bean.setContactPoints(cassandraHost);
        bean.setPort(cassandraPort);
        return bean;
    }

}
