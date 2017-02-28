package com.keifer.core.cache.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.keifer.core.cache.CompositeCache;

import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-cache-redis-cluster.xml" })
public class RedisClusterTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	CompositeCache compositeCache;

	@Test
	public void set() {
		System.out.println(compositeCache.getClusterNodes().size());
		boolean returnResult = compositeCache.set("name", "keifer123");
		Assert.assertEquals(true, returnResult);
	}

	@Test
	public void get() {
		String result = compositeCache.getString("name");
		Assert.assertEquals("keifer123", result);
	}

	@Test
	public void getTargetObjct() {
		JedisCluster result = compositeCache.getRedisClusterTargetObject();
		Assert.assertNotNull(result);
	}
}
