package batch.blogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component("sample001")
public class Sample001BLogic implements Tasklet {

	private static Logger logger = LoggerFactory.getLogger(Sample001BLogic.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		logger.info("Sample001BLogic start");

		return RepeatStatus.FINISHED;
	}

}
