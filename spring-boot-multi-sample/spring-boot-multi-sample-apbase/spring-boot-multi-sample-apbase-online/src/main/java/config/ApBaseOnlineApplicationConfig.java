package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApBaseOnlineApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApBaseCommonApplicationConfig.class);

    public ApBaseOnlineApplicationConfig() {
        logger.trace("ApBaseOnlineApplicationConfig");
    }

}
