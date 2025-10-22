package com.fq.rpc.provider.impl;

import cn.hutool.core.collection.CollUtil;
import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleServiceProviderImpl implements ServiceProvider {
    private final Map<String, Object> SERVICE_CACHE = new HashMap<>();

    @Override
    public void publishService(RpcServiceConfig config) {
        List<String> rpcServiceNames = config.rpcServiceNames();

        if(CollUtil.isEmpty(rpcServiceNames)){
            throw new RuntimeException("该服务没有实现接口");
        }
        log.debug("发布服务: {}", rpcServiceNames);

        rpcServiceNames.forEach(names -> SERVICE_CACHE.put(names, config.getService()));

    }

    @Override
    public Object getService(String rpcServiceName) {
        if(!SERVICE_CACHE.containsKey(rpcServiceName)){
            throw new IllegalArgumentException("找不到对应服务: " + rpcServiceName);
        }
        return SERVICE_CACHE.get(rpcServiceName);
    }
}
