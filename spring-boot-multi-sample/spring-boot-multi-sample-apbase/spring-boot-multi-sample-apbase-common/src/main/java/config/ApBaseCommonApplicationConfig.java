package config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ApBaseCommonApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApBaseCommonApplicationConfig.class);

    public ApBaseCommonApplicationConfig() {
        logger.trace("ApBaseCommonApplicationConfig");
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("dataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    DataSource primaryDataSource(@Named("primaryDataSourceProperties") DataSourceProperties dataSourceProperties) {
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
        if (StringUtils.hasText(dataSourceProperties.getName())) {
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        return dataSource;
    }

    /**
     * インメモリDBの設定.
     */
    @Configuration
    @ConditionalOnProperty(prefix = "envprop.apbase.common", name = "in-memory.enable", havingValue = "true")
    @Import(InMemoryDataSourceConfig.DataSourceInitializer.class)
    public static class InMemoryDataSourceConfig {

        public InMemoryDataSourceConfig() {
            logger.trace("InMemoryDataSourceConfig");
        }

        @Bean("inMemoryDataSourceProperties")
        @ConfigurationProperties(prefix = "spring.datasource.in-memory")
        DataSourceProperties inMemoryDataSourceProperties() {
            return new DataSourceProperties();
        }

        @Bean("inMemoryDataSource")
        DataSource inMemoryDataSource(@Named("inMemoryDataSourceProperties") DataSourceProperties properties) {
            HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                    .type(HikariDataSource.class).build();
            if (StringUtils.hasText(properties.getName())) {
                dataSource.setPoolName(properties.getName());
            }
            return dataSource;
        }

        /**
         * DBの初期化.
         * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer
         */
        static class DataSourceInitializer implements InitializingBean {
            private final DataSource dataSource;

            private final DataSourceProperties properties;

            private final ResourceLoader resourceLoader;

            DataSourceInitializer(@Named("inMemoryDataSource") DataSource dataSource,
                    @Named("inMemoryDataSourceProperties") DataSourceProperties properties,
                    ResourceLoader resourceLoader) {
                logger.trace("DataSourceInitializer");
                this.dataSource = dataSource;
                this.properties = properties;
                this.resourceLoader = (resourceLoader != null) ? resourceLoader : new DefaultResourceLoader(null);
            }

            boolean createSchema() {
                List<Resource> scripts = getScripts("spring.datasource.schema", this.properties.getSchema(), "schema");
                if (!scripts.isEmpty()) {
                    if (!isEnabled()) {
                        logger.debug("Initialization disabled (not running DDL scripts)");
                        return false;
                    }
                    String username = this.properties.getSchemaUsername();
                    String password = this.properties.getSchemaPassword();
                    runScripts(scripts, username, password);
                }
                return !scripts.isEmpty();
            }

            private boolean isEnabled() {
                DataSourceInitializationMode mode = this.properties.getInitializationMode();
                if (mode == DataSourceInitializationMode.NEVER) {
                    return false;
                }
                if (mode == DataSourceInitializationMode.EMBEDDED && !isEmbedded()) {
                    return false;
                }
                return true;
            }

            private boolean isEmbedded() {
                try {
                    return EmbeddedDatabaseConnection.isEmbedded(this.dataSource);
                } catch (Exception ex) {
                    logger.debug("Could not determine if datasource is embedded", ex);
                    return false;
                }
            }

            private List<Resource> getScripts(String propertyName, List<String> resources, String fallback) {
                if (resources != null) {
                    return getResources(propertyName, resources, true);
                }
                String platform = this.properties.getPlatform();
                List<String> fallbackResources = new ArrayList<>();
                fallbackResources.add("classpath*:" + fallback + "-" + platform + ".sql");
                fallbackResources.add("classpath*:" + fallback + ".sql");
                return getResources(propertyName, fallbackResources, false);
            }

            private List<Resource> getResources(String propertyName, List<String> locations, boolean validate) {
                List<Resource> resources = new ArrayList<>();
                for (String location : locations) {
                    for (Resource resource : doGetResources(location)) {
                        if (resource.exists()) {
                            resources.add(resource);
                        } else if (validate) {
                            throw new InvalidConfigurationPropertyValueException(propertyName, resource,
                                    "The specified resource does not exist.");
                        }
                    }
                }
                return resources;
            }

            private Resource[] doGetResources(String location) {
                try {
                    SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(this.resourceLoader,
                            Collections.singletonList(location));
                    factory.afterPropertiesSet();
                    return factory.getObject();
                } catch (Exception ex) {
                    throw new IllegalStateException("Unable to load resources from " + location, ex);
                }
            }

            private void runScripts(List<Resource> resources, String username, String password) {
                if (resources.isEmpty()) {
                    return;
                }
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.setContinueOnError(this.properties.isContinueOnError());
                populator.setSeparator(this.properties.getSeparator());
                if (this.properties.getSqlScriptEncoding() != null) {
                    populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
                }
                for (Resource resource : resources) {
                    populator.addScript(resource);
                }
                DataSource dataSource = this.dataSource;
                if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
                    dataSource = DataSourceBuilder.create(this.properties.getClassLoader())
                            .driverClassName(this.properties.determineDriverClassName())
                            .url(this.properties.determineUrl())
                            .username(username).password(password).build();
                }
                DatabasePopulatorUtils.execute(populator, dataSource);
            }

            @Override
            public void afterPropertiesSet() throws Exception {
                createSchema();
            }

        }

    }

}
