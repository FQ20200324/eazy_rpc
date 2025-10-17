package com.fq.rpc.trans.socket.server;

import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;
import com.fq.rpc.handler.RpcReqHandler;
import com.fq.rpc.provider.ServiceProvider;
import com.fq.rpc.provider.impl.SimpleServiceProvider;
import com.fq.rpc.trans.RpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketRpcServer implements RpcServer {
    private final int             port;
    private final RpcReqHandler   rpcReqHandler;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer(int port) {
        this(port, new SimpleServiceProvider());
    }

    public SocketRpcServer(int port, ServiceProvider provider) {
        this.port = port;
        this.rpcReqHandler = new RpcReqHandler(provider);
        this.serviceProvider = provider;
    }

    @Override
    public void strat() {
        log.info("服务已启动，端口: {}", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                // 拿到请求数据
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                RpcRequest        rpcRequest  = (RpcRequest) inputStream.readObject();
                // 调用相应方法
                Object data = rpcReqHandler.invoke(rpcRequest);

                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                RpcResp<?>         rpcResp      = RpcResp.success(rpcRequest.getReqId(), data);
                outputStream.writeObject(rpcResp);
                outputStream.flush();
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
