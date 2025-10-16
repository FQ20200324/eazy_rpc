package com.fq.rpc.trans;

import com.fq.rpc.config.RpcServiceConfig;

public interface RpcServer {
    void strat();
    void publishService(RpcServiceConfig config);
}
