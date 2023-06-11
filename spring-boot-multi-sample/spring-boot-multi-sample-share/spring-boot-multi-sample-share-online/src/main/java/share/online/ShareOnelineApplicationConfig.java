package share.online;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import apbase.online.ApBaseOnlineApplicationConfig;
import share.common.ShareCommonApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import({ ShareCommonApplicationConfig.class, ApBaseOnlineApplicationConfig.class })
@ComponentScan
public class ShareOnelineApplicationConfig {

    private Logger logger = LoggerFactory.getLogger(ShareOnelineApplicationConfig.class);

    public ShareOnelineApplicationConfig() {
        logger.trace("### ShareOnelineApplicationConfig");
    }

}
