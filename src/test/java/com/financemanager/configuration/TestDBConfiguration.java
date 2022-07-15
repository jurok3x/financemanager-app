package com.financemanager.configuration;

import com.financemanager.repository.UserRepository;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@TestConfiguration
@Import(value = {UserRepository.class})
public class TestDBConfiguration {
    
    @Bean
    public DataSource getDataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/db/user/schema.sql")
                .addScript("classpath:/db/user/test-data.sql")
                .addScript("classpath:/db/users_categories/schema.sql")
                .addScript("classpath:/db/users_categories/test-data.sql")
                .addScript("classpath:/db/role/schema.sql")
                .addScript("classpath:/db/role/test-data.sql")
                .addScript("classpath:/db/category/schema.sql")
                .addScript("classpath:/db/category/test-data.sql")
                .addScript("classpath:/db/expenses/schema.sql")
                .addScript("classpath:/db/expenses/test-data.sql")
                .build();
        return dataSource;
    }
    
}
