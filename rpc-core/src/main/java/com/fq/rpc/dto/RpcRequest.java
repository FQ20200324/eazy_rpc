package com.fq.rpc.dto;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 实现序列化以在网络中传输
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    @Serial
    private static final long       serialVersionUID = 1L;
    private              String     reqId;
    // 找对应接口的实现类
    private              String     interFaceName;
    private              String     methodName;
    // 接口参数及类型
    private              Object[]   params;
    private              Class<?>[] paramTypes;
    // eg: UserService -> CommonUserServiceImpl1.getUser();
    //                 -> CommonUserServiceImpl2.getUser();
    private              String     version;
    // eg: UserService -> CommonUserServiceImpl.getUser();
    //                 -> AdminUserServiceImpl.getUser();
    private              String     group;


    public String rpcServiceName(){
        return getInterFaceName() +
                "_version:" + StrUtil.blankToDefault(getVersion(), StrUtil.EMPTY) +
                "_group:" + StrUtil.blankToDefault(getGroup(), StrUtil.EMPTY);
    }

}
