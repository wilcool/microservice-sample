/**
 * 
 */
package org.zdxue.microservice.xxx.schedule.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zdxue
 */
@Configuration
@ConfigurationProperties(prefix = "xxx.schedule.cron")
public class XxxScheduleCronProperties {

    private String demo;

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

}
