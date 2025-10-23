package com.fq.rpc.trans.socket.client;

import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;
import com.fq.rpc.factory.SingletonFactory;
import com.fq.rpc.registry.ServiceDiscovery;
import com.fq.rpc.registry.impl.ZkServiceDiscoveryImpl;
import com.fq.rpc.trans.RpcClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class SocketRpcClient implements RpcClient {
    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this(SingletonFactory.getInstance(ZkServiceDiscoveryImpl.class));
    }
    public SocketRpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }


    @Override
    public RpcResp<?> sendReq(RpcRequest req) {
        InetSocketAddress address = serviceDiscovery.searchService(req);
        try (Socket socket = new Socket(address.getHostString(), address.getPort())) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(req);
            outputStream.flush();

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object            o           = inputStream.readObject();

            return (RpcResp<?>) o;

        } catch (Exception e) {
            log.error("发送rpc请求失败: {}", e.getMessage(), e);
            log.error("发送rpc请求失败: {}", e.getMessage());
            log.error("发送rpc请求失败: ", e);
        }


        return null;
    }
}
