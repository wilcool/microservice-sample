/**
 * 
 */
package org.zdxue.microservice.xxx.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * @author zdxue
 *
 */
@Configuration
public class DruidMonitor {
    
    @Autowired
    private DruidMonitorConfig druidMonitorConfig;

    @Bean
    public ServletRegistrationBean<StatViewServlet> registrationBean() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<StatViewServlet>(new StatViewServlet(), "/druid/*");
        bean.addInitParameter("allow", druidMonitorConfig.getAllow());
        bean.addInitParameter("deny", druidMonitorConfig.getDeny());
        bean.addInitParameter("loginUsername", druidMonitorConfig.getLoginUsername());
        bean.addInitParameter("loginPassword", druidMonitorConfig.getLoginPassword());
        bean.addInitParameter("resetEnable", druidMonitorConfig.getResetEnable());
        return bean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<WebStatFilter>(new WebStatFilter());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("exclusions", druidMonitorConfig.getExclusions());
        return bean;
    }

}
