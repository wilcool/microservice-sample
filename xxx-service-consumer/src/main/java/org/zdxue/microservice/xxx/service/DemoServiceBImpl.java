/**
 * 
 */
package org.zdxue.microservice.xxx.service;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author zdxue
 */
@Service(provider = "bProvider", protocol = { "bProto" }, registry = { "zkReg" })
@Component
public class DemoServiceBImpl implements DemoService {

    @Override
    public String demo() {
        return "i am demo B";
    }

}
