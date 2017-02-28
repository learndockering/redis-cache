package com.keifer.core.cache.redis.cluster;

import com.keifer.core.cache.Cache;

import redis.clients.jedis.JedisCluster;

/**
 * @author keifer
 */
public interface ClusterRedisCache extends Cache<JedisCluster> {
}
