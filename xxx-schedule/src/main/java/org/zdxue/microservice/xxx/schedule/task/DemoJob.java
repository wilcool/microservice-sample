/**
 * 
 */
package org.zdxue.microservice.xxx.schedule.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author zdxue
 */
@DisallowConcurrentExecution
public class DemoJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.debug("job running...");
    }

}