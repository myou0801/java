package config;

import javax.inject.Named;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableBatchProcessing
public class ApBaseBatchApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApBaseCommonApplicationConfig.class);

    public ApBaseBatchApplicationConfig() {
        logger.trace("ApBaseBatchApplicationConfig");
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.batch-admin")
    DataSourceProperties batchAdminDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("batchAdminDataSource")
    @BatchDataSource
    @ConfigurationProperties("spring.datasource.batch-admin.hikari")
    DataSource batchAdminDataSource(
            @Named("batchAdminDataSourceProperties") DataSourceProperties dataSourceProperties) {
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
        if (StringUtils.hasText(dataSourceProperties.getName())) {
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        return dataSource;
    }

    @Configuration
    @Profile("local")
    public static class LocalBatchConfig extends BasicBatchConfigurer {

        protected LocalBatchConfig(BatchProperties properties, DataSource dataSource,
                TransactionManagerCustomizers transactionManagerCustomizers) {
            super(properties, dataSource, transactionManagerCustomizers);
        }

        @Override
        protected JobRepository createJobRepository() throws Exception {
            MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
            factory.setTransactionManager(super.getTransactionManager());
            return factory.getObject();
        }

    }

    @Configuration
    @Profile("asyncbatch")
    public class AsyncBatchApplicationConfig {

    }

}
