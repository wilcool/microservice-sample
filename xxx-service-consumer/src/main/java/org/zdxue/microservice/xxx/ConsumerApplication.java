package org.zdxue.microservice.xxx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;

/**
 * @author xuezd
 */
@SpringBootApplication
@EnableDubboConfiguration
@MapperScan("org.zdxue.microservice.xxx.mybatis.mapper")
@ComponentScan(basePackages = "org.zdxue.microservice.xxx")
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
