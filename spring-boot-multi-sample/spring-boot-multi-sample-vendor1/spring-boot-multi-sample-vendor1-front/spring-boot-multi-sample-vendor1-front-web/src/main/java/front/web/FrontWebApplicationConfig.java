package front.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import front.domain.FrontDomainApplicationConfig;
import share.online.ShareOnelineApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import({ FrontDomainApplicationConfig.class, ShareOnelineApplicationConfig.class })
@ComponentScan
public class FrontWebApplicationConfig {

    private Logger logger = LoggerFactory.getLogger(FrontWebApplicationConfig.class);

    public FrontWebApplicationConfig() {
        logger.trace("### FrontWebApplicationConfig");
    }

}
