package com.keifer.core.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.keifer.core.cache.utils.LoggerUtil;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author keifer
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultCacheImpl implements CompositeCache, ApplicationContextAware {
	private String kvCacheType;
	private String deployType;
	private ApplicationContext applicationContext;
	private Cache cache;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setKvCacheType(String kvCacheType) {
		this.kvCacheType = kvCacheType;
	}

	public String getKvCacheType() {
		if (StringUtils.isBlank(this.kvCacheType)) {
			this.kvCacheType = KvCacheType.REDIS.getType();
			LoggerUtil.getCacheLogger().warn("Use default value:" + KvCacheType.REDIS.toString());
		}
		return kvCacheType;
	}

	public String getDeployType() {
		if (StringUtils.isBlank(this.deployType)) {
			LoggerUtil.getCacheLogger().warn("Use default value:" + DeployType.SINGLETON.toString());
			this.deployType = DeployType.SINGLETON.getType();
		}
		return deployType;
	}

	public void setDeployType(String deployType) {
		this.deployType = deployType;
	}

	public void init() {
		KvCacheType expectedKvType = KvCacheType.getKvCacheType(getKvCacheType(), getDeployType());
		try {
			cache = (Cache) applicationContext.getBean(expectedKvType.getTargetClazz());
		} catch (NoSuchBeanDefinitionException e) {
			throw new NoSuchBeanDefinitionException(expectedKvType.getTargetClazz().getInterfaces()[0].getSimpleName(),
					"Cache instance init error: expecetd bean:" + expectedKvType.getTargetClazz().getName()
							+ " instance not found.");
		}
	}

	public Cache getCache() {
		return cache;
	}

	@Override
	public Object get(String key) {
		return this.getCache().get(key);
	}

	@Override
	public Object hget(String hashtable, String key) {
		return this.getCache().hget(hashtable, key);
	}

	@Override
	public boolean remove(String key) {
		return this.getCache().remove(key);
	}

	@Override
	public boolean hset(String hashtable, String key, String value) {
		return this.getCache().hset(hashtable, key, value);
	}

	@Override
	public boolean set(String key, Object value) {
		return this.getCache().set(key, value);
	}

	@Override
	public boolean set(String key, Object value, Integer expiredTime) {
		return this.getCache().set(key, value, expiredTime);
	}

	@Override
	public String getString(String key) {
		return this.getCache().getString(key);
	}

	@Override
	public String hgetString(String hashtable, String key) {
		return this.getCache().hgetString(hashtable, key);
	}

	@Override
	public boolean hset(String hashtable, String key, Object value) {
		return this.getCache().hset(hashtable, key, value);
	}

	@Override
	public boolean set(String key, String value) {
		return this.getCache().set(key, value);
	}

	@Override
	public Map<String, Object> getClusterNodes() {
		return this.getCache().getClusterNodes();
	}

	@Override
	public Object getTargetObject() {
		return this.getCache().getTargetObject();
	}

	@Override
	public ShardedJedisPool getSharedRedisTargetObject() {
		return (ShardedJedisPool) getTargetObject();
	}

	@Override
	public JedisCluster getRedisClusterTargetObject() {
		return (JedisCluster) getTargetObject();
	}

	@Override
	public JedisPool getRedisTargetObject() {
		return (JedisPool) getTargetObject();
	}

	@Override
	public boolean lset(String key, int expiredTime, String... values) {
		return this.getCache().lset(key, expiredTime, values);
	}

	@Override
	public long llen(String key) {
		return this.getCache().llen(key);
	}

	@Override
	public String lget(String key) {
		return this.getCache().lget(key);
	}

	@Override
	public boolean hremove(String hashtable, String key) {
		return this.getCache().hremove(hashtable, key);
	}

	@Override
	public List hvalues(String hashtable) {
		return this.getCache().hvalues(hashtable);
	}
}
