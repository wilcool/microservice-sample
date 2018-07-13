package org.zdxue.sso;

import org.I0Itec.zkclient.ZkClient;
import org.zdxue.microservice.xxx.common.util.HttpClientUtil;

public class ZkClientTest {

    public static void main(String[] args) throws Exception {
        ZkClient zkClient = new ZkClient("192.168.56.10:2181", 3000);
        zkClient.subscribeChildChanges("/hello", (parentPath, currentChilds) -> {
            System.out.println("parentPath：" + parentPath);
            System.out.println("currentChilds：" + currentChilds);
        });


        zkClient.createPersistent("/hello");
        zkClient.writeData("/hello", "hihi");

        zkClient.createPersistent("/hello/abc");
        zkClient.writeData("/hello/abc", "hoho");

        String s = zkClient.readData("/dubbo");
        System.out.println(s);
    }

}