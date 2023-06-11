package front.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import share.ShareApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import({ ShareApplicationConfig.class })
@ComponentScan
public class FrontDomainApplicationConfig {

    private Logger logger = LoggerFactory.getLogger(FrontDomainApplicationConfig.class);

    public FrontDomainApplicationConfig() {
        logger.trace("### FrontDomainApplicationConfig");
    }

}
