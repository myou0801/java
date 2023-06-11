package apbase.online;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import apbase.common.ApBaseCommonApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import(ApBaseCommonApplicationConfig.class)
@ComponentScan
public class ApBaseOnlineApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApBaseOnlineApplicationConfig.class);

    public ApBaseOnlineApplicationConfig() {
        logger.trace("### ApBaseOnlineApplicationConfig");
    }

    // session-jdbcを使わない（ローカル環境ではnone）
    //    /**
    //     * セッションがJDBCの場合の設定。
    //     * <p>
    //     * データソースの設定、初期テーブルの設定。
    //     */
    //    @ConditionalOnProperty(prefix = "spring.session", name = "store-type", havingValue = "jdbc")
    //    public class JdbcSessionConfig {
    //
    //        @Bean
    //        @SpringSessionDataSource
    //        DataSource springSessionDataSource(@Named("inMemoryDataSource") ObjectProvider<DataSource> inMemoryDataSource,
    //                ObjectProvider<DataSource> dataSource) {
    //
    //            DataSource dataSourceToUse = inMemoryDataSource.getIfAvailable(() -> dataSource.getObject());
    //            return dataSourceToUse;
    //        }
    //
    //        @Bean
    //        JdbcSessionDataSourceInitializer jdbcSessionDataSourceInitializer(
    //                @SpringSessionDataSource ObjectProvider<DataSource> springSessionDataSource,
    //                ObjectProvider<DataSource> dataSource,
    //                ResourceLoader resourceLoader, JdbcSessionProperties properties) {
    //
    //            DataSource dataSourceToUse = springSessionDataSource.getIfAvailable(() -> dataSource.getObject());
    //            return new JdbcSessionDataSourceInitializer(dataSourceToUse, resourceLoader, properties);
    //        }
    //
    //    }

}
