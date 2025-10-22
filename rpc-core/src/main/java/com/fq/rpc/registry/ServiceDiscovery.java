package com.fq.rpc.registry;

import com.fq.rpc.dto.RpcRequest;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    InetSocketAddress searchService(RpcRequest rpcReq);

}
