package com.kingstar.indicatorservice.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class RedisUtil {


    @Value("${custom.prefix.expire_time}")
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

    public String hget(String key, String item) {
        try {
            return jedisCluster.hget(key,item);
        }catch (Exception e){
            log.error("error in RedisUtil.hget",e);
            return null;
        }
    }


    public Set<String> hkeys(String key) {
        try {
            return jedisCluster.hkeys(key);
        }catch (Exception e){
            log.error("error in RedisUtil.hkeys",e);
            return null;
        }
    }

    public Map<String, String> hgetAll(String key) {
        try {
            return jedisCluster.hgetAll(key);
        }catch (Exception e){
            log.error("error in RedisUtil.hgetAll",e);
            return null;
        }

    }
}