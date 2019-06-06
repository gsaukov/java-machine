package com.apps.searchandpagination.cassandra.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import java.io.IOException;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS geoKeySpace " + "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

    public static final String KEYSPACE_ACTIVATE_QUERY = "USE geoKeySpace;";

    @Override
    protected String getKeyspaceName() {
        return "geoKeySpace";
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        startCassandraEmbedded();
        CassandraClusterFactoryBean cluster =
                new CassandraClusterFactoryBean();
        cluster.setContactPoints("127.0.0.1");
        cluster.setPort(9142);
        return cluster;
    }


    public void startCassandraEmbedded() {
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra();
            final Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9142).build();
            final Session session = cluster.connect();
            session.execute(KEYSPACE_CREATION_QUERY);
            session.execute(KEYSPACE_ACTIVATE_QUERY);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}