package com.fq.rpc.provider.impl;

import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.constant.RpcConstant;
import com.fq.rpc.factory.SingletonFactory;
import com.fq.rpc.provider.ServiceProvider;
import com.fq.rpc.registry.ServiceRegistry;
import com.fq.rpc.registry.impl.ZkServiceRegistryImpl;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
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
        config.rpcServiceNames().forEach(rpcServiceName -> {
                    // 存入<serviceName， service>
                    SERVICE_CACHE.put(rpcServiceName, config.getService());
                    // 获取address和port
            String            hostAddress       = null;
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            int               port              = RpcConstant.port;
                    InetSocketAddress address = new InetSocketAddress(hostAddress, port);
                    serviceRegistry.registerService(rpcServiceName, address);

                }

        );
    }

    @Override
    public Object getService(String rpcServiceName) {
        return null;
    }
}
