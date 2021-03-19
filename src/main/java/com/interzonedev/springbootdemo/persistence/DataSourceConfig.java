package com.interzonedev.springbootdemo.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuration class for the {@link DataSource} used by the application.
 */
@Configuration("springbootdemo.persistence.dataSource.config")
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    private final Environment environment;

    @Inject
    public DataSourceConfig(Environment environment) throws URISyntaxException {
        this.environment = environment;
    }

    /**
     * Creates the {@link DataSource} for use across the application.  The URL of the {@link DataSource} is created by
     * parsing the {@code DATABASE_URL} environment variable.  The connection pool class name, database URL scheme and
     * database driver class name are read from the {@code application.properties} file.
     *
     * @return Returns a {@link DataSource} for use across the application.
     *
     * @throws URISyntaxException Thrown if a {@link URI} could not be created from the value of the
     * {@code DATABASE_URL} environment variable.
     * @throws ClassNotFoundException Thrown if the connetion pool class could not be found on the classpath.
     */
    @ConfigurationProperties(prefix = "springbootdemo.persistence.datasource")
    @Bean(name = "springbootdemo.persistence.dataSource")
    public DataSource getDataSource() throws URISyntaxException, ClassNotFoundException {
        String databaseURLFromEnv = environment.getProperty("DATABASE_URL");
        String connectionPoolClassName = environment.getProperty("springbootdemo.persistence.datasource.connection-pool-class-name");
        String dbScheme = environment.getProperty("springbootdemo.persistence.datasource.scheme");
        String dbDriverClassName = environment.getProperty("springbootdemo.persistence.datasource.driver-class-name");

        log.debug("getDataSource: databaseURLFromEnv = " + databaseURLFromEnv);

        URI databaseURI = new URI(databaseURLFromEnv);

        @SuppressWarnings("unchecked")
        Class<? extends DataSource> connectionPoolClass = (Class<? extends DataSource>) Class.forName(connectionPoolClassName);

        String dbURL = "jdbc:" + dbScheme + databaseURI.getHost() + databaseURI.getPath();

        String dbUserInfo = databaseURI.getUserInfo();
        String[] userInfoComponents = dbUserInfo.split(":");
        String dbUsername = userInfoComponents[0];
        String dbPassword = userInfoComponents[1];

        return DataSourceBuilder
                .create()
                .type(connectionPoolClass)
                .driverClassName(dbDriverClassName)
                .url(dbURL)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}
