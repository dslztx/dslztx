package me.dslztx.assist.client.redis;

import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lambdaworks.redis.RedisFuture;

import me.dslztx.assist.algorithm.loadbalance.AbstractLoadBalancer;
import me.dslztx.assist.algorithm.loadbalance.LeastActiveLoadBalancer;
import me.dslztx.assist.algorithm.loadbalance.LoadBalancerEnum;
import me.dslztx.assist.util.ConfigLoadAssist;
import me.dslztx.assist.util.ObjectAssist;
import me.dslztx.assist.util.StringAssist;

public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    private static final String CONFIG_FILE = "redis_service.properties";

    private static AbstractLoadBalancer<LettuceAsyncClientProxy> loadBalancer = null;

    static {
        initLoadBalancer();
    }

    private static void initLoadBalancer() {
        try {
            Configuration configuration = ConfigLoadAssist.propConfig(CONFIG_FILE);

            String loadBalancerType = configuration.getString("loadbalancer");

            if (StringAssist.isBlank(loadBalancerType)) {
                throw new RuntimeException("no loadbalancer type specified");
            }

            if (LoadBalancerEnum.Random.toString().equalsIgnoreCase(loadBalancerType)) {
            } else if (LoadBalancerEnum.RoundRobin.toString().equalsIgnoreCase(loadBalancerType)) {
            } else if (LoadBalancerEnum.LeastActive.toString().equalsIgnoreCase(loadBalancerType)) {
                loadBalancer = new LeastActiveLoadBalancer<LettuceAsyncClientProxy>();
            } else if (LoadBalancerEnum.ConsistentHash.toString().equalsIgnoreCase(loadBalancerType)) {
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException(e);
        } finally {
            if (loadBalancer == null) {
                loadBalancer = new LeastActiveLoadBalancer<LettuceAsyncClientProxy>();
            }
        }
    }

    public static RedisFutureProxy<List<String>> mgetAsync(String redisClusterName, String... keys) {

        LettuceAsyncClientProxy client =
            loadBalancer.select(LettuceAsyncClientFactory.obtainRedisClient(redisClusterName));

        if (ObjectAssist.isNull(client)) {
            return null;
        }

        RedisFuture<List<String>> result = client.getRedisAsyncConnection().mget(keys);

        client.incActive();

        return new RedisFutureProxy<List<String>>(result, client);
    }

    public static List<String> mgetSync(String redisClusterName, String... keys) {
        throw new UnsupportedOperationException("sync operation is not supported now");
    }
}
