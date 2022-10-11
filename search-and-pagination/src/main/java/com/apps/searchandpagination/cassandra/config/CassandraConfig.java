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
    public CqlSessionFactoryBean cluster() {
        CqlSessionFactoryBean cluster =
                new CqlSessionFactoryBean();
        //can be more than one contact point
        cluster.setContactPoints(cassandraHost);
        cluster.setPort(cassandraPort);
        return cluster;
    }

}