package com.prs.rs.config;


import static com.prs.rs.common.ConstantValues.READ_DB;
import static com.prs.rs.common.ConstantValues.WRITE_DB;

import com.prs.rs.source.ReplicationRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ReplicaConfig {

    @Value("${spring.datasource.write.url}")
    private String writeUrl;

    @Value("${spring.datasource.read.url}")
    private String readUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;

    @Bean
    public DataSource writeDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .url(writeUrl)
            .driverClassName(driverName)
            .password(password)
            .username(username)
            .build();
    }

    @Bean
    public DataSource readDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .url(readUrl)
            .driverClassName(driverName)
            .password(password)
            .username(username)
            .build();
    }

    @Bean
    public DataSource routingDataSource() {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<Object, Object>();
        dataSourceMap.put(WRITE_DB, writeDataSource());
        dataSourceMap.put(READ_DB, readDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(writeDataSource());

        return routingDataSource;

    }

    @Bean
    @Primary
    public DataSource routingLazyDataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(routingLazyDataSource());
        return transactionManager;
    }
}
