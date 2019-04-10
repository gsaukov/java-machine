package com.apps.t24app.persistance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DataSourceConfig.class)
//@EnableJpaRepositories
//@ConfigurationProperties(prefix = "someconfig")
public class EntityManagerConfig {

//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    public EntityManager entityManager() {
//
//    }
//
//    @Bean
//    public EntityManagerFactory entityManagerFactory() {
//
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//
//    }

}
