package org.zdxue.microservice.xxx.cache.redis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zdxue.microservice.xxx.common.StringHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author zdxue
 */
@Component
public class ShardedJedisAgent {
    
    private JedisPoolConfig poolConf;
    
    private JedisPool pool;
    private ShardedJedisPool shardingPool;
    
    @Autowired
    private JedisConfig jedisConfig;

    @PostConstruct
    private void init() {
        initPoolConf();
        
        pool = new JedisPool(
                poolConf, 
                jedisConfig.getHost(), 
                jedisConfig.getPort(), 
                jedisConfig.getConnectionTimeout(),
                jedisConfig.getPassword(), 
                jedisConfig.getDatabase());

        List<JedisShardInfo> shardingNodes = new ArrayList<JedisShardInfo>();
        String[] shardingHosts = StringHelper.toStringArray(jedisConfig.getShardingHost());
        for(String host : shardingHosts) {
            JedisShardInfo shardInfo = new JedisShardInfo(host);
            shardInfo.setConnectionTimeout(3000);
            shardInfo.setSoTimeout(1000);
            shardingNodes.add(shardInfo);
        }
        shardingPool = new ShardedJedisPool(poolConf, shardingNodes);
    }

    private void initPoolConf() {
        poolConf = new JedisPoolConfig();
        poolConf.setTestOnBorrow(jedisConfig.isTestOnBorrow());
        poolConf.setTestOnReturn(jedisConfig.isTestOnReturn());
        poolConf.setTestWhileIdle(jedisConfig.isTestWhileIdle());
        poolConf.setBlockWhenExhausted(jedisConfig.isBlockWhenExhausted());
        poolConf.setMaxTotal(jedisConfig.getMaxTotal());
        poolConf.setMaxIdle(jedisConfig.getMaxIdle());
        poolConf.setMinIdle(jedisConfig.getMinIdle());
        poolConf.setMaxWaitMillis(jedisConfig.getMaxWaitMillis());
        poolConf.setNumTestsPerEvictionRun(jedisConfig.getNumTestsPerEvictionRun());
        poolConf.setTimeBetweenEvictionRunsMillis(jedisConfig.getTimeBetweenEvictionRunsMillis());
        poolConf.setMinEvictableIdleTimeMillis(jedisConfig.getMinEvictableIdleTimeMillis());
        poolConf.setSoftMinEvictableIdleTimeMillis(jedisConfig.getSoftMinEvictableIdleTimeMillis());
    }
    
    public Jedis getJedis() {
        return pool.getResource();
    }

    public ShardedJedis getShardedJedis() {
        return shardingPool.getResource();
    }

}