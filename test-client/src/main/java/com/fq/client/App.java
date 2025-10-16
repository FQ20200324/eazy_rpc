package com.fq.client;

import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;
import com.fq.rpc.trans.RpcClient;
import com.fq.rpc.trans.socket.client.SocketRpcClient;

public class App {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketRpcClient("127.0.0.1", 8888);
        RpcRequest req = RpcRequest.builder()
                .reqId("111")
                .interFaceName("com.fq.api.UserService")
                .methodName("getUser")
                .params(new Object[]{1L})
                .paramTypes(new Class[]{Long.class})
                .build();

        RpcResp<?> rpcResp = rpcClient.sendReq(req);
        System.out.println(rpcResp.getData());

    }

//    private static <T> T invoke(Long id) {
//        RpcClient rpcClient = new SocketRpcClient("127.0.0.1", 8888);
//        RpcRequest req = RpcRequest.builder()
//                .reqId("111")
//                .interFaceName("com.fq.api.UserService")
//                .methodName("getUser")
//                .params(new Object[]{id})
//                .paramTypes(new Class[]{Long.class})
//                .build();
//
//        RpcResp<?> rpcResp = rpcClient.sendReq(req);
//        return (T) rpcResp.getData();
//    }
}
