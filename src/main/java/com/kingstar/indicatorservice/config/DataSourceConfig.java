package com.kingstar.indicatorservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2019/9/24 9:53
 */
@Configuration
@Slf4j
public class DataSourceConfig {


    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${spring.datasource.hikari.auto-commit}")
    private boolean autoCommit;
    @Value("${spring.datasource.hikari.idle-timeout}")
    private long idleTimeout;
//    @Value("${spring.datasource.hikari.pool-name}")
//    private String poolName;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private long connectionTimeout;


    @Bean
    public DataSource datasource(){
        log.info("init db pool.........");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setDriverClassName(driver);
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setAutoCommit(autoCommit);
        config.setIdleTimeout(idleTimeout);
//        config.setPoolName(poolName);
        config.setConnectionTimeout(connectionTimeout);
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }


}
