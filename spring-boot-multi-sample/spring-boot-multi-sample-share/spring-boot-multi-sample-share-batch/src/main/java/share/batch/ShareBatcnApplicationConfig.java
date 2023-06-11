package share.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import apbase.batch.ApBaseBatchApplicationConfig;
import share.common.ShareCommonApplicationConfig;

@Configuration(proxyBeanMethods = false)
@Import({ ShareCommonApplicationConfig.class, ApBaseBatchApplicationConfig.class })
@ComponentScan
public class ShareBatcnApplicationConfig {

    private Logger logger = LoggerFactory.getLogger(ShareBatcnApplicationConfig.class);

    public ShareBatcnApplicationConfig() {
        logger.trace("### ShareBatcnApplicationConfig");
    }

}
