package com.kingstar.indicatorservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

@Component
@SuppressWarnings("unchecked")
public class RedisUtil {

    private static Logger log = LoggerFactory.getLogger(RedisUtil.class);

    @Value("${customize.prefix.expire_time}")
    private Integer expire_time;
	
	@Autowired
    private JedisCluster jedisCluster;  //自动释放连接


    public boolean hset(String key, String item, String value) {
        try {
            jedisCluster.hset(key,item,value);
            jedisCluster.expire(key,expire_time);
            return true;
        } catch (Exception e) {
            log.error("error in RedisUtil.hset",e);
            return false;
        }
    }

    public Object hget(String key, String item) {
        try {
            return jedisCluster.hget(key,item);
        }catch (Exception e){
            log.error("error in RedisUtil.hget",e);
            return null;
        }
    }





}