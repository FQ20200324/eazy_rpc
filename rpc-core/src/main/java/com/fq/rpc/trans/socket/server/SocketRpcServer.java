package com.fq.rpc.trans.socket.server;

import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;
import com.fq.rpc.trans.RpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketRpcServer implements RpcServer {
    private final int port;

    public SocketRpcServer(int port) {
        this.port = port;
    }

    @Override
    public void strat() {
        log.info("服务已启动，端口: {}", port);
        try (ServerSocket serverSocket = new ServerSocket(port)){
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                // 拿到请求数据
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                RpcRequest        rpcRequest  = (RpcRequest) inputStream.readObject();
                System.out.println(rpcRequest);
                // 调用相应方法，这里假装拿到了
                String data = "拿到了数据:aaabbc";

                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                RpcResp<String>    rpcResp      = RpcResp.success(rpcRequest.getReqId(), data);
                outputStream.writeObject(rpcResp);
                outputStream.flush();
            }

        }catch (Exception e){
            log.error("服务端异常", e);
        }
    }
}
