package com.apps.cloud.common.data.audit;

import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseMetricsConfigurer implements BeanPostProcessor {

    @Bean
    public QueryExecutionListener queryExecutionListener() {
        return new DatabaseMetricsCollector();
    }

}