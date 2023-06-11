package apbase.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobLoggingListener implements JobExecutionListener {

	private static Logger logger = LoggerFactory.getLogger(JobLoggingListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("start job " + jobExecution.toString());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		logger.info("end job " + jobExecution.toString());
	}

}
