package org.zdxue.sso;

import org.zdxue.microservice.xxx.common.util.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

public class HttpTest {

    public static void main(String[] args) throws Exception {
        String rsp = HttpClientUtil.get("https://www.baidu.com/");
        System.out.println(rsp);
    }

}