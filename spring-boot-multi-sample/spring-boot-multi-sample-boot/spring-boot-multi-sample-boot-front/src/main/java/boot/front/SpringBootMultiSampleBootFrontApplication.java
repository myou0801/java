package boot.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "config" })
public class SpringBootMultiSampleBootFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMultiSampleBootFrontApplication.class, args);
	}

}
