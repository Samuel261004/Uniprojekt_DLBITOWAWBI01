package com.example.demo.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.example.demo.model")
@PropertySource("classpath:application.properties")
public class PersistenceJpaConfig {
    @Value("jdbc:h2:file:./src/main/resources/data/data;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE;DB_CLOSE_DELAY=1")
    private String datasourceUrl;
    @Value("org.h2.Driver")
    private String driverClassName;
    @Value("sa")
    private String username;
    @Value("")
    private String password;
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(datasourceUrl).
                driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.example.demo.model");
        factoryBean.setPersistenceUnitName("h2PU");
        factoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        return factoryBean;
    }


    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

