package com.fq.rpc.proxy;

import cn.hutool.core.util.IdUtil;
import com.fq.rpc.config.RpcServiceConfig;
import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;
import com.fq.rpc.enums.RpcRespStatus;
import com.fq.rpc.exception.RpcException;
import com.fq.rpc.trans.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class RpcClientProxy implements InvocationHandler {
    private final RpcClient        rpcClient;
    private final RpcServiceConfig config;

    public RpcClientProxy(RpcClient rpcClient) {
        this(rpcClient, new RpcServiceConfig());
    }

    public RpcClientProxy(RpcClient rpcClient, RpcServiceConfig config) {
        this.rpcClient = rpcClient;
        this.config = config;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                this);
    }

    // 代理对象/代理方法/参数
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        System.out.println("执行方法前");

        RpcRequest req = RpcRequest.builder()
                .reqId(IdUtil.fastSimpleUUID())
                .interFaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .version(config.getVersion())
                .group(config.getGroup())
                .build();

        RpcResp<?> rpcResp = rpcClient.sendReq(req);
        check(req, rpcResp);
        System.out.println("执行方法后");

        return rpcResp.getData();
    }


    private void check(RpcRequest req, RpcResp<?> rsp) {
        if (Objects.isNull(rsp)) {
            throw new RpcException("rpcResp 为空");
        }
        // Objects.equals含有非空判断，以避免空指针
        if (!Objects.equals(rsp.getReqId(), req.getReqId())) {
            throw new RpcException("请求和响应的ID不一致");
        }
        if (!RpcRespStatus.isSuccessful(rsp.getCode())) {
            throw new RpcException("响应为失败: " + rsp.getMsg());
        }
    }
}
