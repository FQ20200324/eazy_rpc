package com.fq.server;

import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.trans.RpcServer;
import com.fq.rpc.trans.socket.server.SocketRpcServer;
import com.fq.server.service.UserServiceImpl;

public class App {
    public static void main(String[] args) {
        RpcServiceConfig config = new RpcServiceConfig(new UserServiceImpl());

        RpcServer rpcServer = new SocketRpcServer(8888);
        rpcServer.publishService(config);

        rpcServer.strat();




    }
}
