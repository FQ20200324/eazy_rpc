package com.fq.rpc.registry.impl;

import cn.hutool.core.util.StrUtil;
import com.fq.rpc.config.ZkClient;
import com.fq.rpc.constant.RpcConstant;
import com.fq.rpc.factory.SingletonFactory;
import com.fq.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {
    private final ZkClient zkClient;

    public ZkServiceRegistryImpl(){
        this(SingletonFactory.getInstance(ZkClient.class));
    }
    public ZkServiceRegistryImpl(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress address) {
        log.info("服务注册, rpcServiceName: {}, address: {}", rpcServiceName, address);
        String path =  RpcConstant.ZK_RPC_ROOT_PATH + StrUtil.SLASH + rpcServiceName + StrUtil.SLASH + address.getHostString() + StrUtil.COLON + address.getPort();

        zkClient.createPersistentNode(path);
        System.out.println(path);
    }
}
