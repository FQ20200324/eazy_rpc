package com.fq.rpc.trans.socket.server;

import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;
import com.fq.rpc.provider.ServiceProvider;
import com.fq.rpc.provider.impl.SimpleServiceProvider;
import com.fq.rpc.trans.RpcServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketRpcServer implements RpcServer {
    private final int             port;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer(int port) {
        this(port, new SimpleServiceProvider());
    }

    public SocketRpcServer(int port, ServiceProvider provider) {
        this.port = port;
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
                Object data    = invoke(rpcRequest);

                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                RpcResp<?>    rpcResp      = RpcResp.success(rpcRequest.getReqId(), data);
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

    //@SneakyThrows 自动try-catch抛出异常
    @SneakyThrows
    private Object invoke(RpcRequest req) {
        String name    = req.rpcServiceName();
        Object service = serviceProvider.getService(name);
        log.debug("获取服务: {}", service.getClass().getCanonicalName());
        Method method = service.getClass().getMethod(req.getMethodName());
        return method.invoke(service, req.getParams());
    }
}
