/**
 * 
 */
package org.zdxue.microservice.xxx.service;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author zdxue
 */
@Service(provider = "aProvider", protocol = { "aProto" }, registry = { "localReg" })
@Component
public class DemoServiceAImpl implements DemoService {

    @Override
    public String demo() {
        return "i am demo A";
    }

}
