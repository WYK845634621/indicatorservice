package com.kingstar.indicatorservice.web;

import com.kingstar.indicatorservice.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/3/25 9:31
 */
@RestController
@Slf4j
public class HelloController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/hello")
    public String h(){
        redisUtil.hset("kk","ii","vv");
        log.info("==============");
        return "hello";
    }

}
