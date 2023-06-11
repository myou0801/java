package apbase.common;

import java.util.List;

import javax.inject.Named;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.AbstractDataSourceInitializer;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class ApBaseCommonApplicationConfig {

	private static Logger logger = LoggerFactory.getLogger(ApBaseCommonApplicationConfig.class);

	public ApBaseCommonApplicationConfig() {
		logger.trace("### ApBaseCommonApplicationConfig");
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
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(prefix = "envprop.apbase.common", name = "in-memory.enable", havingValue = "true")
	public static class InMemoryDataSourceConfig {

		public InMemoryDataSourceConfig() {
			logger.trace("### InMemoryDataSourceConfig");
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

		@Bean
		InMemoryDataSourceInitializer inMemoryDataSourceInitializer(
				@Named("inMemoryDataSource") DataSource inMemoryDataSource,
				@Named("inMemoryDataSourceProperties") DataSourceProperties inMemoryDataSourceProperties,
				ResourceLoader resourceLoader) {
			return new InMemoryDataSourceInitializer(inMemoryDataSource, inMemoryDataSourceProperties, resourceLoader);
		}

		/**
		 * DBの初期化.
		 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer
		 */
		static class InMemoryDataSourceInitializer extends AbstractDataSourceInitializer {

			private static final String PLATFORM_PLACEHOLDER = "@@platform@@";
			private final DataSourceProperties properties;
			private final ResourceLoader resourceLoader;

			InMemoryDataSourceInitializer(DataSource dataSource,
					DataSourceProperties properties,
					ResourceLoader resourceLoader) {
				super(dataSource, resourceLoader);
				this.properties = properties;
				this.resourceLoader = resourceLoader;
			}

			@Override
			protected void initialize() {
				if (properties.getSchema() == null || properties.getSchema().isEmpty()) {
					return;
				}
				super.initialize();
			}

			@Override
			protected DataSourceInitializationMode getMode() {
				return properties.getInitializationMode();
			}

			@Override
			protected String getSchemaLocation() {
				return properties.getSchema().get(0);
			}

			@Override
			protected void customize(ResourceDatabasePopulator populator) {
				List<String> schemas = properties.getSchema();
				String platform = getDatabaseName();
				if (schemas.size() > 1) {
					populator.setScripts(schemas.stream()
							.map(l -> resourceLoader.getResource(l.replace(PLATFORM_PLACEHOLDER, platform)))
							.toArray(Resource[]::new));
				}

				if (properties.getData() != null && !properties.getData().isEmpty()) {
					populator.addScripts(properties.getData().stream()
							.map(l -> resourceLoader.getResource(l.replace(PLATFORM_PLACEHOLDER, platform)))
							.toArray(Resource[]::new));
				}

			}

		}

	}

	@Configuration(proxyBeanMethods = false)
	public static class ApBaseCommonThymeleaf implements ApplicationContextAware {

		private ApplicationContext applicationContext;

		@Primary
		@Bean
		@ConfigurationProperties(prefix = "spring.thymeleaf")
		ThymeleafProperties thymeleafProperties() {
			return new ThymeleafProperties();
		}

		/**
		 * メールテンプレートをThymeleafを使う場合のプロパティ設定読み込み。
		 * @return プロパティ
		 */
		@Bean("mailTemplateThymeleafProperties")
		@ConditionalOnProperty(prefix = "spring.thymeleaf", name = "mail")
		@ConfigurationProperties(prefix = "spring.thymeleaf.mail")
		ThymeleafProperties mailTemplateThymeleafProperties() {
			return new ThymeleafProperties();
		}

		/**
		 * メールテンプレートをThymeleafを使う場合。
		 * @param properties プロパティ
		 * @return SpringResourceTemplateResolvers
		 */
		@Bean("mailTemplateResolver")
		@ConditionalOnBean(name = "mailTemplateThymeleafProperties")
		SpringResourceTemplateResolver mailTemplateResolver(
				@Named("mailTemplateThymeleafProperties") ThymeleafProperties properties) {
			SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
			resolver.setApplicationContext(this.applicationContext);
			resolver.setPrefix(properties.getPrefix());
			resolver.setSuffix(properties.getSuffix());
			resolver.setTemplateMode(properties.getMode());
			if (properties.getEncoding() != null) {
				resolver.setCharacterEncoding(properties.getEncoding().name());
			}
			resolver.setCacheable(properties.isCache());
			Integer order = properties.getTemplateResolverOrder();
			if (order != null) {
				resolver.setOrder(order);
			}
			resolver.setCheckExistence(properties.isCheckTemplate());
			return resolver;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}

	}

	@Configuration(proxyBeanMethods = false)
	public static class ApBaseCommonAws {

		/**
		 * localstackとか使用する場合のS3クライアントの作成。
		 * <p>
		 * {@code cloud.aws.endpoint}でEndpoint(URL)を設定した場合に作成。
		 * @param serviceEndpoint 接続先URL
		 * @param signingRegion リージョン
		 * @return AmazonS3オブジェクト
		 */
		@Bean
		@ConditionalOnProperty(prefix = "cloud.aws", name = "endpoint")
		AmazonS3 amazonS3(@Value("${cloud.aws.endpoint}") String serviceEndpoint,
				@Value("${cloud.aws.region.static:ap-northeast-1}") String signingRegion) {
			return AmazonS3ClientBuilder.standard()
					.withEndpointConfiguration(new EndpointConfiguration(serviceEndpoint, signingRegion))
					.withPathStyleAccessEnabled(true) // これがないとローカル環境ではエラーになるので注意
					.build();
		}

	}

}
