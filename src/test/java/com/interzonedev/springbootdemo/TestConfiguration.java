package com.interzonedev.springbootdemo;

import com.interzonedev.zankou.dataset.dbunit.DbUnitDataSetTester;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for beans used in the integration tests.
 */
@Configuration("springbootdemo.test.config")
public class TestConfiguration {

    /**
     * Gets the {@link DbUnitDataSetTester} instance to be used across the application integration tests.
     *
     * @return Returns a {@link DbUnitDataSetTester} instance.
     */
    @Bean(name = "springbootdemo.test.dbUnitDataSetTester")
    public DbUnitDataSetTester getDbUnitDataSetTester() {
        return new DbUnitDataSetTester();
    }

}
