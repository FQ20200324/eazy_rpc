package com.fq.client.util;

import com.fq.rpc.proxy.RpcClientProxy;
import com.fq.rpc.trans.socket.client.SocketRpcClient;

public class ProxyUtil {

    private static final SocketRpcClient rpcClient      = new SocketRpcClient();
    private static final RpcClientProxy  rpcClientProxy = new RpcClientProxy(rpcClient);

    public static <T> T getProxy(Class<T> clazz) {
        return rpcClientProxy.getProxy(clazz);
    }

}
