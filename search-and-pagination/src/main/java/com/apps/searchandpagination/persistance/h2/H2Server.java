package com.apps.searchandpagination.persistance.h2;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2Server {

//    new conn string jdbc:h2:tcp://localhost:9090/file:./data/db/spDb
//    conn string jdbc:h2:tcp://localhost:9090/mem:spDb
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }

}
