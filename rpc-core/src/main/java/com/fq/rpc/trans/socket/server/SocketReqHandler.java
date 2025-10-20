package com.fq.rpc.trans.socket.server;

import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;
import com.fq.rpc.handler.RpcReqHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
@AllArgsConstructor
public class SocketReqHandler implements Runnable {
    private final Socket        socket;
    private final RpcReqHandler rpcReqHandler;

    @SneakyThrows
    @Override
    public void run() {
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
}
