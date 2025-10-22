package com.fq.rpc.loadbalance.impl;

import cn.hutool.core.util.RandomUtil;
import com.fq.rpc.loadbalance.LoadBalance;

import java.util.List;

public class RandomLoadBalanceImpl implements LoadBalance {
    @Override
    public String select(List<String> list) {
        return RandomUtil.randomEle(list);
    }
}
