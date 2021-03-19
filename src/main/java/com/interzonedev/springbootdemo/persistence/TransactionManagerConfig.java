package com.interzonedev.springbootdemo.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 * Configuration class for the {@link PlatformTransactionManager} used by the application.
 */
@Configuration("springbootdemo.persistence.transactionManager.config")
public class TransactionManagerConfig {

    private final DataSource dataSource;

    @Inject
    public TransactionManagerConfig(@Named("springbootdemo.persistence.dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets the {@link PlatformTransactionManager} instance to be used across the service layer of the application.
     *
     * @return Returns a {@link JpaTransactionManager} instance.
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

}
