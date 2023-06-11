package boot.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import front.web.FrontWebApplicationConfig;

@SpringBootApplication
@Import(FrontWebApplicationConfig.class)
public class SpringBootMultiSampleBootFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMultiSampleBootFrontApplication.class, args);
    }

}
