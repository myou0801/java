package apbase.batch;

import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import com.zaxxer.hikari.HikariDataSource;

import apbase.batch.listener.JobLoggingListener;
import apbase.batch.listener.StepLoggingListener;
import apbase.common.ApBaseCommonApplicationConfig;
import apbase.env.AsyncBatch;
import apbase.env.SyncBatch;

@Configuration(proxyBeanMethods = false)
@Import(ApBaseCommonApplicationConfig.class)
public class ApBaseBatchApplicationConfig {

	private static Logger logger = LoggerFactory.getLogger(ApBaseBatchApplicationConfig.class);

	public ApBaseBatchApplicationConfig() {
		logger.trace("### ApBaseBatchApplicationConfig");
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

	@Bean("batchAdminTransactionManager")
	PlatformTransactionManager batchAdminTransactionManager(@Named("batchAdminDataSource") DataSource dataSource,
			ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		JdbcTransactionManager transactionManager = new JdbcTransactionManager(dataSource);
		transactionManager.setRollbackOnCommitFailure(true);
		transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
		return transactionManager;
	}

	@Bean
	DataSourceInitializer batchAdminDataSourceInitializer(@Named("batchAdminDataSource") DataSource dataSource,
			@Named("batchAdminDataSourceProperties") DataSourceProperties dataSourceProperties,
			ResourceLoader resourceLoader) {
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);

		if (isEnabled(dataSource, dataSourceProperties)) {

			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			if (dataSourceProperties.getSchema() != null && !dataSourceProperties.getSchema().isEmpty()) {
				populator.addScripts(dataSourceProperties.getSchema().stream()
						.map(l -> resourceLoader.getResource(l))
						.toArray(Resource[]::new));
			}

			if (dataSourceProperties.getData() != null && !dataSourceProperties.getData().isEmpty()) {
				populator.addScripts(dataSourceProperties.getData().stream()
						.map(l -> resourceLoader.getResource(l))
						.toArray(Resource[]::new));
			}
			dataSourceInitializer.setDatabasePopulator(populator);
		}

		return dataSourceInitializer;
	}

	private boolean isEnabled(DataSource dataSource, DataSourceProperties dataSourceProperties) {
		DataSourceInitializationMode mode = dataSourceProperties.getInitializationMode();
		if (mode == DataSourceInitializationMode.NEVER) {
			return false;
		}
		if (mode == DataSourceInitializationMode.EMBEDDED && !isEmbedded(dataSource)) {
			return false;
		}
		return true;
	}

	private boolean isEmbedded(DataSource dataSource) {
		try {
			return EmbeddedDatabaseConnection.isEmbedded(dataSource);
		} catch (Exception ex) {
			logger.debug("Could not determine if datasource is embedded", ex);
			return false;
		}
	}

	@Configuration(proxyBeanMethods = false)
	public static class BatchMybatisConfig extends MybatisAutoConfiguration {

		public BatchMybatisConfig(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
				ObjectProvider<TypeHandler[]> typeHandlersProvider,
				ObjectProvider<LanguageDriver[]> languageDriversProvider, ResourceLoader resourceLoader,
				ObjectProvider<DatabaseIdProvider> databaseIdProvider,
				ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
			super(properties, interceptorsProvider, typeHandlersProvider, languageDriversProvider, resourceLoader,
					databaseIdProvider, configurationCustomizersProvider);
		}

		@Bean("sqlSessionFactory")
		@Primary
		@Override
		public SqlSessionFactory sqlSessionFactory(@Named("dataSource") DataSource dataSource)
				throws Exception {
			return super.sqlSessionFactory(dataSource);
		}

		@Bean("sqlSessionTemplate")
		@Primary
		@Override
		public SqlSessionTemplate sqlSessionTemplate(@Named("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
			return super.sqlSessionTemplate(sqlSessionFactory);
		}

		@Bean("batchAdminSqlSessionFactory")
		public SqlSessionFactory batchAdminSqlSessionFactory(@Named("batchAdminDataSource") DataSource dataSource)
				throws Exception {
			return super.sqlSessionFactory(dataSource);
		}

		@Bean("batchAdminSqlSessionTemplate")
		public SqlSessionTemplate batchAdminSqlSessionTemplate(
				@Named("batchAdminSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
			return super.sqlSessionTemplate(sqlSessionFactory);
		}

	}

	@Bean
	JobExecutionListener jobLoggingListener() {
		return new JobLoggingListener();
	}

	@Bean
	StepExecutionListener stepLoggingListener() {
		return new StepLoggingListener();
	}

	// JobRepositoryのインメモリはh2の方がよいのでコメントアウト
	//	/**
	//	 * JobRepositoryをDBではなくインメモリにするための設定。
	//	 */
	//	@Configuration(proxyBeanMethods = false)
	//	@ConditionalOnProperty(prefix = "envprop.apbase.batch.job-repository.in-memory", name = "enable", havingValue = "true")
	//	public static class JobRepositoryInMemoryConfig extends BasicBatchConfigurer {
	//
	//		protected JobRepositoryInMemoryConfig(BatchProperties properties, DataSource dataSource,
	//				TransactionManagerCustomizers transactionManagerCustomizers) {
	//			super(properties, dataSource, transactionManagerCustomizers);
	//			logger.trace("### JobRepositoryInMemoryConfig");
	//		}
	//
	//		@Override
	//		protected JobRepository createJobRepository() throws Exception {
	//			MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
	//			factory.setTransactionManager(super.getTransactionManager());
	//			return factory.getObject();
	//		}
	//
	//		@Bean
	//		BatchDataSourceInitializer batchDataSourceInitializer(DataSource dataSource,
	//				@BatchDataSource ObjectProvider<DataSource> batchDataSource, ResourceLoader resourceLoader,
	//				BatchProperties properties) {
	//			return new BatchDataSourceInitializer(batchDataSource.getIfAvailable(() -> dataSource), resourceLoader,
	//					properties) {
	//				@Override
	//				public void afterPropertiesSet() {
	//					// 何もしない
	//				}
	//			};
	//		}
	//	}

	@Configuration(proxyBeanMethods = false)
	@EnableBatchProcessing
	@SyncBatch
	public class SyncBatchApplicationconfig {
		public SyncBatchApplicationconfig() {
			logger.trace("### SyncBatchApplicationconfig");
		}
	}

	@Configuration(proxyBeanMethods = false)
	@EnableBatchProcessing(modular = true)
	@MapperScan(basePackages = "org.terasoluna.batch.async.db.repository", sqlSessionFactoryRef = "batchAdminSqlSessionFactory", sqlSessionTemplateRef = "batchAdminSqlSessionTemplate")
	@EnableScheduling
	@AsyncBatch
	public class AsyncBatchApplicationConfig implements ApplicationContextAware {

		private ApplicationContext applicationContext;

		public AsyncBatchApplicationConfig() {
			logger.trace("### AsyncBatchApplicationConfig");
		}

//		@Bean
//		ApplicationContextFactory applicationContextFactory() throws ClassNotFoundException {
//			return new GenericApplicationContextFactory(
//					applicationContext.getBeansOfType(BatchJobConfig.class).values().stream()
//							.map(conf -> conf.getClass()).toArray());
//		}

		// TODO TERASOLUNA Batch
		//		@Bean
		//		JobRequestPollTask jobRequestPollTask(BatchJobRequestRepository batchJobRequestRepository,
		//				@Named("batchAdminTransactionManager") PlatformTransactionManager transactionManager,
		//				ThreadPoolTaskExecutor daemonTaskExecutor,
		//				JobOperator jobOperator, AutomaticJobRegistrar automaticJobRegistrar) {
		//			JobRequestPollTask task = new JobRequestPollTask(batchJobRequestRepository, transactionManager,
		//					daemonTaskExecutor, jobOperator, automaticJobRegistrar);
		//
		//			return task;
		//		}
		//
		//		@Bean
		//		AsyncBatchDaemon deamon() {
		//			return new AsyncBatchDaemon();
		//		}
		//
		//		@Bean
		//		ApplicationRunner applicationRunner(AsyncBatchDaemon deamon) {
		//			return arg -> deamon.main(new String[] {});
		//		}

//		@Bean
//		public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
//			JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
//			postProcessor.setJobRegistry(jobRegistry);
//			return postProcessor;
//		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}

	}

}
