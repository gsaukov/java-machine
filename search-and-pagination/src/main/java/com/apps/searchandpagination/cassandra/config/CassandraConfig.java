package com.apps.searchandpagination.cassandra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

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
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster =
                new CassandraClusterFactoryBean();
        //can be more than one contact point
        cluster.setContactPoints(cassandraHost);
        cluster.setPort(cassandraPort);
        return cluster;
    }

}