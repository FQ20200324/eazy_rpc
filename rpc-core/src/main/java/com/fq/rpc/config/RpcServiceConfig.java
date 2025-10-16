package com.fq.rpc.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcServiceConfig {
    // 用于找到唯一的服务方法
    private String version = "";
    private String group   = "";
    private Object service;


    public RpcServiceConfig(Object service) {
        this.service = service;
    }

    public List<String> rpcServiceNames() {
        return interfaceName().stream()
                .map(name -> name +" version:"+ version +" group:"+ group)
                .toList();
    }

    private List<String> interfaceName() {
        return Arrays.stream(service.getClass().getInterfaces())
                .map(Class::getCanonicalName)
                .toList();
    }


}
