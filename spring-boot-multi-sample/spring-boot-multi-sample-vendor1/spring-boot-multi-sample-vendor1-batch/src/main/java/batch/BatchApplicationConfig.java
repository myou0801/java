package batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import apbase.batch.BatchJobConfig;
import apbase.env.AsyncBatch;
import apbase.env.SyncBatch;
import share.ShareApplicationConfig;
import share.batch.ShareBatcnApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import({ ShareApplicationConfig.class, ShareBatcnApplicationConfig.class })
@ComponentScan
public class BatchApplicationConfig {

	private Logger logger = LoggerFactory.getLogger(BatchApplicationConfig.class);

	public BatchApplicationConfig() {
		logger.trace("### BatchApplicationConfig");
	}

	/**
	 * 同期バッチのジョブ定義。
	 */
	@Configuration(proxyBeanMethods = false)
	@SyncBatch
	public class SyncBatchJobConfig extends BatchJobConfig {
		@Bean
		public Job job001() {
			return createJob("job001", "sample001");
		}

		@Bean
		public Job job002() {
			return createJob("job002", "sample002");
		}
	}

	/**
	 * 非同期バッチのジョブ定義。
	 */
	@Configuration(proxyBeanMethods = false)
	@AsyncBatch
	public class AsyncBatchJobConfig extends BatchJobConfig {

		@Bean
		public Job job003() {
			return createJob("job003", "sample001");
		}

		@Bean
		public Job job004() {
			return createJob("job004", "sample002");
		}

	}
	
	
	@Configuration(proxyBeanMethods = false)
	@AsyncBatch
	public class AsyncBatchJobConfig2 extends BatchJobConfig {

		@Bean
		public Job job001() {
			return createJob("job001", "sample001");
		}

		@Bean
		public Job job002() {
			return createJob("job002", "sample002");
		}

	}

}
