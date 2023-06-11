package apbase.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepLoggingListener implements StepExecutionListener {

	private static Logger logger = LoggerFactory.getLogger(StepLoggingListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("start step " + stepExecution.toString());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("end step " + stepExecution.toString());
		return stepExecution.getExitStatus();
	}

}
