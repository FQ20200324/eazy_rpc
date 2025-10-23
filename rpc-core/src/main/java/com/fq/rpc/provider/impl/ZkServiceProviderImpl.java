package com.fq.rpc.provider.impl;

import cn.hutool.core.util.StrUtil;
import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.constant.RpcConstant;
import com.fq.rpc.factory.SingletonFactory;
import com.fq.rpc.provider.ServiceProvider;
import com.fq.rpc.registry.ServiceRegistry;
import com.fq.rpc.registry.impl.ZkServiceRegistryImpl;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ZkServiceProviderImpl implements ServiceProvider {
    private final Map<String, Object> SERVICE_CACHE = new HashMap<>();
    private final ServiceRegistry     serviceRegistry;

    public ZkServiceProviderImpl() {
        this(SingletonFactory.getInstance(ZkServiceRegistryImpl.class));
    }

    public ZkServiceProviderImpl(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void publishService(RpcServiceConfig config) {
        config.rpcServiceNames().forEach(rpcServiceName -> publishService(config, rpcServiceName));
    }

    @SneakyThrows
    private void publishService(RpcServiceConfig config, String rpcServiceName) {
        // 获取address和port 注册到zk
        String            hostAddress = InetAddress.getLocalHost().getHostAddress();
        int               port        = RpcConstant.port;
        InetSocketAddress address     = new InetSocketAddress(hostAddress, port);
        serviceRegistry.registerService(rpcServiceName, address);
        // 存入<serviceName， service>
        SERVICE_CACHE.put(rpcServiceName, config.getService());
    }

    @Override
    public Object getService(String rpcServiceName) {
        if (StrUtil.isBlank(rpcServiceName)) {
            throw new IllegalArgumentException("rpcServiceName is blank");
        }

        if(!SERVICE_CACHE.containsKey(rpcServiceName)){
            throw new IllegalArgumentException("rpcServiceName 未注册: " + rpcServiceName);
        }
        return SERVICE_CACHE.get(rpcServiceName);
    }
}
