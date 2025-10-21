package com.fq.client;

import com.fq.api.UserService;
import com.fq.client.util.ProxyUtil;

public class App {
    public static void main(String[] args) {
        //获取代理类
        UserService proxy = ProxyUtil.getProxy(UserService.class);
        //invoke 调用远程方法被拦截
        System.out.println(proxy.getUser(1L));

    }

}
