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
import java.io.IOException;

//https://shermandigital.com/blog/designing-a-cassandra-data-model/
//https://stackoverflow.com/questions/24949676/difference-between-partition-key-composite-key-and-clustering-key-in-cassandra
@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS geoKeySpace " + "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

    public static final String KEYSPACE_ACTIVATE_QUERY = "USE geoKeySpace;";

    public static final String KEYSPACE_CREATE_ADDRESS_DATA = "CREATE TABLE IF NOT EXISTS address_data ( " +
            "            address_id text, " +
            "            account_id text, " +
            "            longitude double, " +
            "            latitude double, " +
            "            place_id bigint, " +
            "            osm_type text, " +
            "            osm_id text, " +
            "            bounding_box map<text, double>, " +
            "            polygon_points frozen <list <map <text, double>>>, " +
            "            display_name text, " +
            "            element_class text, " +
            "            element_type text, " +
            "            address_elements map<text, text>, " +
            "            name_details map<text, text>, " +
            "            place_rank int, " +
            "            importance double, " +
            "            wkt text, " +
            "            PRIMARY KEY ((address_id, account_id, longitude, latitude)) " +
            ");";

    @Override
    protected String getKeyspaceName() {
        return "geoKeySpace";
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        startCassandraEmbedded();
        CassandraClusterFactoryBean cluster =
                new CassandraClusterFactoryBean();
        //can be more than one contact point
        cluster.setContactPoints("127.0.0.1");
        cluster.setPort(9042);
        return cluster;
    }


    public void startCassandraEmbedded() {
//        try {
//            EmbeddedCassandraServerHelper.startEmbeddedCassandra();
//        } catch (TTransportException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
//        }
        Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9042).build();
        Session session = cluster.connect();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute("DROP TABLE  geoKeySpace.address_data");
        session.execute(KEYSPACE_ACTIVATE_QUERY);
        session.execute(KEYSPACE_CREATE_ADDRESS_DATA);
    }
}