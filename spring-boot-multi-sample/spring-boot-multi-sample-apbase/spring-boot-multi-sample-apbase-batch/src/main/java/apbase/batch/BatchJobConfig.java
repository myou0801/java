package apbase.batch;

import javax.inject.Inject;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BatchJobConfig implements ApplicationContextAware {

	@Inject
	private JobBuilderFactory jobs;

	@Inject
	private StepBuilderFactory steps;

	@Inject
	private JobExecutionListener jobLoggingListener;

	@Inject
	private StepExecutionListener stepLoggingListener;

	private ApplicationContext applicationContext;

	protected Job createJob(String jobId, String batchId) {
		return createJobBuilder(jobId)
				.start(createTaskletStepBuilder(jobId, batchId).build())
				.build();
	}

	protected JobBuilder createJobBuilder(String jobId) {
		return jobs.get(jobId).listener(jobLoggingListener);
	}

	protected TaskletStepBuilder createTaskletStepBuilder(String jobId, String batchId) {
		Tasklet tasklet = applicationContext.getBean(batchId, Tasklet.class);
		return (TaskletStepBuilder) steps.get(jobId + "." + batchId).tasklet(tasklet).listener(stepLoggingListener);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
