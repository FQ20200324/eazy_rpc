package com.fq.server;

import com.fq.rpc.trans.RpcServer;
import com.fq.rpc.trans.socket.server.SocketRpcServer;

public class App {
    public static void main(String[] args) {
        RpcServer rpcServer = new SocketRpcServer(8888);

        rpcServer.strat();
    }
}
