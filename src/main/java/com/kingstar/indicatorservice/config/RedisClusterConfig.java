package com.kingstar.indicatorservice.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/3/24 10:15
 */
@Component
@ConfigurationProperties(prefix = "redis.cluster")
@Slf4j
public class RedisClusterConfig {


    private String[] nodes;


    private int timeOut;

    private int maxAttempts;

    private int soTimeOut;

    private int maxIdle;

    private long maxWaitMillis;

    public String[] getNodes() {
        return nodes;
    }

    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }


    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getSoTimeOut() {
        return soTimeOut;
    }

    public void setSoTimeOut(int soTimeOut) {
        this.soTimeOut = soTimeOut;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    @Bean
    public JedisCluster redisClusterFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        Set<HostAndPort> hostAndPorts = new HashSet<>(1<<4);
        for (String str:nodes){
            log.info("redis node info: ------  " + str);
            hostAndPorts.add(new HostAndPort(str.split(":")[0], Integer.valueOf(str.split(":")[1])));
        }
        return new JedisCluster(hostAndPorts, timeOut, soTimeOut, maxAttempts, jedisPoolConfig);
    }


}
