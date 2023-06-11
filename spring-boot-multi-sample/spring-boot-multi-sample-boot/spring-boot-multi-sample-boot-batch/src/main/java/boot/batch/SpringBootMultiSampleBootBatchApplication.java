package boot.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import batch.BatchApplicationConfig;

@SpringBootApplication
@Import({ BatchApplicationConfig.class })
public class SpringBootMultiSampleBootBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMultiSampleBootBatchApplication.class, args);
    }

}
