/**
 * 
 */
package org.zdxue.microservice.xxx.schedule.configuration;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zdxue.microservice.xxx.schedule.task.DemoJob;

/**
 * @author zdxue
 */
@Configuration
public class ScheduleConfiguration {

    @Bean
    public JobDetail demoJobDetail() {
        return JobBuilder.newJob(DemoJob.class).withIdentity("demoJob").storeDurably().build();
    }

    @Bean
    public Trigger demoJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");
        return TriggerBuilder.newTrigger().forJob(demoJobDetail()).withIdentity("demoJobTrigger")
                .withSchedule(scheduleBuilder).build();
    }

}
