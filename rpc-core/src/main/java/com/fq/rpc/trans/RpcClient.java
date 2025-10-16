package com.fq.rpc.trans;

import com.fq.rpc.dto.RpcRequest;
import com.fq.rpc.dto.RpcResp;

public interface RpcClient {
    RpcResp<?> sendReq(RpcRequest req);
}
