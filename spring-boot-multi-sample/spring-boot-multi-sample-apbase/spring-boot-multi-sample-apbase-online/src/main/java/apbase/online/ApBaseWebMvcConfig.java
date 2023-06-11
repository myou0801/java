package apbase.online;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

@Configuration(proxyBeanMethods = false)
public class ApBaseWebMvcConfig extends DelegatingWebMvcConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ApBaseWebMvcConfig.class);

    public ApBaseWebMvcConfig() {
        logger.trace("### ApBaseWebMvcConfig");
    }

}
