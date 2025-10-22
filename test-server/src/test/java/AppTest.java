import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class AppTest {

    @SneakyThrows
    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);

        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(retryPolicy)
                .build();

        zkClient.start();

        // 所监听的节点
        String path = "/node1";

        NodeCache nodeCache = new NodeCache(zkClient, path);

        NodeCacheListener listener = () -> {
            if (nodeCache.getCurrentData() != null && nodeCache.getCurrentData().getData() != null) {
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println("节点数据变化: " + data);
            } else if (nodeCache.getCurrentData() != null) {
                System.out.println("节点数据为空");
            } else {
                System.out.println("节点被删除");
            }
        };

        nodeCache.getListenable().addListener(listener);

        nodeCache.start();

        // 防止程序退出
        System.in.read();


        // 创建持久化（默认持久化）节点
        // 当父节点不存在时自动创建父节点
//        zkClient.create().creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .forPath("/node1/003", "测试数据".getBytes(StandardCharsets.UTF_8));

//        // 查看是否创建成功
//        Stat stat = zkClient.checkExists().forPath("/node1/003");
//        System.out.println(stat != null);
//
//        if (zkClient.getZookeeperClient().isConnected()) {
//            System.out.println("当前连接状态: 已连接");
//        }
//
//        byte[] bytes = zkClient.getData().forPath("/node1/002");
//        System.out.println(new String(bytes, StandardCharsets.UTF_8));
//
//        zkClient.setData().forPath("/node1/002", "qqqq2".getBytes(StandardCharsets.UTF_8));
//
//        zkClient.delete().deletingChildrenIfNeeded().forPath("/node1");


    }
}
