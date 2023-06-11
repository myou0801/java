package share.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import apbase.common.ApBaseCommonApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import(ApBaseCommonApplicationConfig.class)
@ComponentScan
public class ShareCommonApplicationConfig {

    private Logger logger = LoggerFactory.getLogger(ShareCommonApplicationConfig.class);

    public ShareCommonApplicationConfig() {
        logger.trace("### ShareCommonApplicationConfig");
    }

}
