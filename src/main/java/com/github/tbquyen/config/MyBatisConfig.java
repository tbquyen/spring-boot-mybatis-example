package com.github.tbquyen.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.github.tbquyen")
public class MyBatisConfig {
  @Value("${database.driverClassName:org.postgresql.Driver}")
  private String driverClassName;
  @Value("${database.url}")
  private String url;
  @Value("${database.username}")
  private String username;
  @Value("${database.password}")
  private String password;
  @Value("${database.maxIdle:2}")
  private int maxIdle;
  @Value("${database.testWhileIdle:true}")
  private boolean testWhileIdle;
  @Value("${database.validationQuery:SELECT 1}")
  private String validationQuery;

  @Bean
  DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMaxIdle(maxIdle);
    dataSource.setMaxWaitMillis(-1);
    dataSource.setTestWhileIdle(testWhileIdle);
    dataSource.setValidationQuery(validationQuery);
    dataSource.setValidationQueryTimeout(0);
    dataSource.setDefaultQueryTimeout(0);
    return dataSource;
  }
}
