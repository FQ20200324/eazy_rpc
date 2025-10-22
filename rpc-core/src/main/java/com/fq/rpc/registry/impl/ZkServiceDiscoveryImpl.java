package com.fq.rpc.registry.impl;

import cn.hutool.core.util.StrUtil;
import com.fq.rpc.config.ZkClient;
import com.fq.rpc.constant.RpcConstant;
import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.factory.SingletonFactory;
import com.fq.rpc.loadbalance.LoadBalance;
import com.fq.rpc.loadbalance.impl.RandomLoadBalanceImpl;
import com.fq.rpc.registry.ServiceDiscovery;
import com.fq.rpc.util.IPUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private final ZkClient    zkClient;
    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this(SingletonFactory.getInstance(ZkClient.class), SingletonFactory.getInstance(RandomLoadBalanceImpl.class));
    }

    public ZkServiceDiscoveryImpl(ZkClient client, LoadBalance loadBalance) {
        this.zkClient = client;
        this.loadBalance = loadBalance;
    }


    @Override
    public InetSocketAddress searchService(RpcRequest rpcReq) {
        String path = RpcConstant.ZK_RPC_ROOT_PATH + StrUtil.SLASH + rpcReq.rpcServiceName();

        List<String> childrenNode = zkClient.getChildrenNode(path);
        String       address       = loadBalance.select(childrenNode);

        return IPUtils.toInetSocketAddress(address);
    }
}
