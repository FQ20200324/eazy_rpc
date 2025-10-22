package com.fq.rpc.config;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

import static com.fq.rpc.constant.RpcConstant.ZK_IP;
import static com.fq.rpc.constant.RpcConstant.ZK_PORT;

@Slf4j
public class ZkClient {
    private final        CuratorFramework client;
    private static final int              BASE_SLEEP_TIME = 1000;
    private static final int              MAX_RETRIES     = 3;

    public ZkClient() {
        this(ZK_IP, ZK_PORT);
    }

    public ZkClient(String hostname, int port) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);

        this.client = CuratorFrameworkFactory.builder()
                .connectString(hostname + StrUtil.COLON + port)
                .retryPolicy(retryPolicy)
                .build();
        log.info("开始连接zk");
        this.client.start();
        log.info("zk链接成功");
    }

    @SneakyThrows
    public void createPersistentNode(String path) {
        if (StrUtil.isBlank(path)) {
            throw new IllegalArgumentException("path为空");
        }

        if (client.checkExists().forPath(path) != null) {
            log.info("该节点已存在");
            return;
        }

        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path);
        log.info("创建节点: {}", path);
    }

    @SneakyThrows
    public List<String> getChildrenNode(String path) {
        if (StrUtil.isBlank(path)) {
            throw new IllegalArgumentException("path为空");
        }
        return client.getChildren().forPath(path);
    }

}
