package com.fq.rpc.trans.socket.server;

import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.constant.RpcConstant;
import com.fq.rpc.handler.RpcReqHandler;
import com.fq.rpc.provider.ServiceProvider;
import com.fq.rpc.provider.impl.SimpleServiceProviderImpl;
import com.fq.rpc.trans.RpcServer;
import com.fq.rpc.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Slf4j
public class SocketRpcServer implements RpcServer {
    private final int             port;
    private final RpcReqHandler   rpcReqHandler;
    private final ServiceProvider serviceProvider;
    private final ExecutorService executor;

    public SocketRpcServer() {
        this(RpcConstant.port);
    }

    public SocketRpcServer(int port) {
        this(port, new SimpleServiceProviderImpl());
    }

    public SocketRpcServer(int port, ServiceProvider provider) {
        this.port = port;
        this.rpcReqHandler = new RpcReqHandler(provider);
        this.serviceProvider = provider;
        this.executor = ThreadPoolUtil.createIoIntensiveThreadPool(
                "socket-rpc-server-"
        );
    }

    @Override
    public void strat() {
        log.info("服务已启动，端口: {}", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                executor.submit(new SocketReqHandler(socket, rpcReqHandler));
            }

        } catch (Exception e) {
            log.error("服务端异常", e);
        }
    }

    @Override
    public void publishService(RpcServiceConfig config) {
        serviceProvider.publishService(config);
    }

}
