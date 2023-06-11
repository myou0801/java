package share;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import share.common.ShareCommonApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import({ ShareCommonApplicationConfig.class })
@ComponentScan
public class ShareApplicationConfig {

    private Logger logger = LoggerFactory.getLogger(ShareApplicationConfig.class);

    public ShareApplicationConfig() {
        logger.trace("### ShareApplicationConfig");
    }

}
