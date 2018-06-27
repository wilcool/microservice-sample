package org.zdxue.microservice.xxx.cache.redis;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zdxue.microservice.xxx.common.StringHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

/**
 * @author zdxue
 */
@Component
public class JedisTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

    private static final long INCR_BY = 1;

    @Autowired
    private ShardedJedisAgent shardedJedisAgent;

    public void set(String key, String value, int expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            shardedJedis.setex(key, expire, value);
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    /**
     * Note: this method only for counter
     * 
     * @return
     */
    public long getCount(String key) {
        return StringHelper.parseLong(get(key), 0);
    }

    public String get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            return shardedJedis.get(key);
        } catch (Exception e) {
            logger.error("occur error.", e);
            return null;
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void incr(String key, int expire) {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.incr(key);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.incr(key);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }
    
    public void incrBy(String key, long delta, int expire) {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.incrBy(key, delta);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.incrBy(key, delta);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void hincr(String key, String field, int expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.hincrBy(key, field, INCR_BY);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.hincrBy(key, field, INCR_BY);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }
    
    public void hincrBy(String key, String field, long delta, int expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.hincrBy(key, field, delta);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.hincrBy(key, field, delta);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void hset(String key, String field, String value, int expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field) || StringUtils.isEmpty(value)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.NO_EXPIRE || ttl == Redis.KEY_NOT_EXIST) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.hset(key, field, value);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.hset(key, field, value);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public String hget(String key, String field) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return null;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            return shardedJedis.hget(key, field);
        } catch (Exception e) {
            logger.error("occur error.", e);
            return null;
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    /**
     * Note: this method only for counter
     * 
     * @param key
     * @param field
     * @return
     */
    public long hgetCount(String key, String field) {
        return StringHelper.parseLong(hget(key, field), 0);
    }

    public Map<String, String> hgetAll(String key) {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptyMap();
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            return shardedJedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("occur error.", e);
            return Collections.emptyMap();
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void lpush(String key, String value, int expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.lpush(key, value);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.lpush(key, value);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void sadd(String key, String value, int expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.sadd(key, value);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.sadd(key, value);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Set<String> smembers(String key) {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            return shardedJedis.smembers(key);
        } catch (Exception e) {
            logger.error("occur error.", e);
            return Collections.emptySet();
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public long zcard(String key) {
        if (StringUtils.isEmpty(key)) {
            return 0;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            return shardedJedis.zcard(key);
        } catch (Exception e) {
            logger.error("occur error.", e);
            return 0;
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void zadd(String key, String value, double score, int expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.zadd(key, score, value);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.zadd(key, score, value);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            return shardedJedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            logger.error("occur error.", e);
            return Collections.emptySet();
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void zremrangeByRank(String key, int start, int end) {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            shardedJedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public void pfadd(String key, int expire, String... elements) {
        if (StringUtils.isEmpty(key) || elements == null || elements.length <= 0) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            long ttl = shardedJedis.ttl(key);
            if (ttl == Redis.KEY_NOT_EXIST || ttl == Redis.NO_EXPIRE) {
                Jedis jedis = shardedJedis.getShard(key);
                Transaction tx = jedis.multi();
                tx.pfadd(key, elements);
                tx.expire(key, expire);
                tx.exec();
            } else {
                shardedJedis.pfadd(key, elements);
            }
        } catch (Exception e) {
            logger.error("occur error.", e);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public long pfcount(String key) {
        if (StringUtils.isEmpty(key)) {
            return 0;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisAgent.getShardedJedis();
            return shardedJedis.pfcount(key);
        } catch (Exception e) {
            logger.error("occur error.", e);
            return 0;
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }
}
