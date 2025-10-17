package com.fq.rpc.handler;

import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.provider.ServiceProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class RpcReqHandler {
    private final ServiceProvider serviceProvider;

    public RpcReqHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    //@SneakyThrows 自动try-catch抛出异常
    @SneakyThrows
    public Object invoke(RpcRequest req) {
        String name    = req.rpcServiceName();
        Object service = serviceProvider.getService(name);
        log.debug("获取服务: {}", service.getClass().getCanonicalName());
        //获取方法需传入参数类型，否则自动获取默认无参方法，就可能获取不到
        Method method = service.getClass().getMethod(req.getMethodName(), req.getParamTypes());
        return method.invoke(service, req.getParams());
    }
}
