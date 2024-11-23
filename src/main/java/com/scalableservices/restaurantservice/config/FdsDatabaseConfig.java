package com.scalableservices.restaurantservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "fdsRestaurantDbTransactionManager",
        basePackages = "com.scalableservices.restaurantservice.repository"
)
@EnableTransactionManagement
public class FdsDatabaseConfig {
    @Primary
    @Bean(name = "fdsRestaurantDb")
    @ConfigurationProperties("spring.fds-restaurant-db.datasource.hikari")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fdsRestaurantDb") DataSource dataSource,
            JpaProperties jpaProperties,
            HibernateProperties hibernateProperties) {
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(),
                new HibernateSettings());

        return builder
                .dataSource(dataSource)
                .packages("com.scalableservices.restaurantservice.model")
                .persistenceUnit("fdsRestaurantDb")
                .properties(properties)
                .build();
    }
    @Primary
    @Bean(name = "fdsRestaurantDbTransactionManager")
    public PlatformTransactionManager fdsRestaurantDbTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
