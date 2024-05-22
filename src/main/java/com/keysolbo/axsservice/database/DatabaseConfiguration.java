package com.keysolbo.axsservice.database;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
@Configuration
public class DatabaseConfiguration {

    @Bean(name = "digitaldb")
    @ConfigurationProperties(prefix = "spring.datasource.digital")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "digitalTemplate")
    public JdbcTemplate jdbcTemplate1(@Qualifier("digitaldb") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
