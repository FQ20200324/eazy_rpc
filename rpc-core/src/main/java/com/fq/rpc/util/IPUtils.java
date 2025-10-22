package com.fq.rpc.util;

import cn.hutool.core.util.StrUtil;

import java.net.InetSocketAddress;

public class IPUtils {


    public static InetSocketAddress toInetSocketAddress(String address){
        if (StrUtil.isBlank(address)){
            throw new IllegalArgumentException("address 不能为空");
        }

        String[] split = address.split(StrUtil.COLON);
        if (split.length != 2){
            throw new IllegalArgumentException("address 格式异常, address: " + address);
        }

        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }
}
