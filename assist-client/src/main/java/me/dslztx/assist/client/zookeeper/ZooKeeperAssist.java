package me.dslztx.assist.client.zookeeper;

import java.util.HashMap;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dslztx.assist.util.ObjectAssist;
import me.dslztx.assist.util.StringAssist;

public class ZooKeeperAssist {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperAssist.class);

    public static Map<String, String> traverseLeafNode(CuratorFramework curatorFramework, String rootPath) {
        Map<String, String> result = new HashMap<String, String>();
        if (ObjectAssist.isNull(curatorFramework) || StringAssist.isBlank(rootPath)) {
            return result;
        }

        StringBuilder sb = new StringBuilder();
        int pos = rootPath.length() - 1;
        while (pos >= 0) {
            if (rootPath.charAt(pos) == '/') {
                pos--;
            } else {
                break;
            }
        }

        if (pos <= 0) {
            // pos=0也不满足，是因为路径必须以“/”开头
            return result;
        }

        rootPath = rootPath.substring(0, pos + 1);

        try {
            traverseLeafNode0(curatorFramework, rootPath, result);
        } catch (Exception e) {
            logger.error("", e);
        }

        return result;
    }

    private static void traverseLeafNode0(CuratorFramework curatorFramework, String path, Map<String, String> map)
        throws Exception {

        String[] childPaths = curatorFramework.getChildren().forPath(path).toArray(new String[0]);
        if (childPaths.length == 0) {
            map.put(path, new String(curatorFramework.getData().forPath(path)));
        }

        for (String childPath : childPaths) {
            traverseLeafNode0(curatorFramework, path + "/" + childPath, map);
        }
    }

    public static void heartbeat(String beatPath) {
        try {
            CuratorFramework curatorFramework = ZooKeeperClientFactory.obtainZooKeeperClient();

            if (curatorFramework.checkExists().forPath(beatPath) != null) {
                // 如果存在先删除，针对“这个路径是上一个会话临时创建的，该会话即将结束，同时该临时路径即将被删除”的情形
                curatorFramework.delete().forPath(beatPath);
                logger.info("delete first successfully if exists");

                curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(beatPath);
                logger.info("create successfully");
            } else {
                curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(beatPath);
                logger.info("create successfully");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void heartbeat(String beatPath, String content) {
        try {
            CuratorFramework curatorFramework = ZooKeeperClientFactory.obtainZooKeeperClient();

            if (curatorFramework.checkExists().forPath(beatPath) != null) {
                // 如果存在先删除，针对“这个路径是上一个会话临时创建的，该会话即将结束，同时该临时路径即将被删除”的情形
                curatorFramework.delete().forPath(beatPath);
                logger.info("delete first successfully if exists");

                curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(beatPath, content.getBytes());
                logger.info("create successfully");
            } else {
                curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(beatPath, content.getBytes());
                logger.info("create successfully");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}