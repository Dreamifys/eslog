package com.brand.log.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RedissonManager {


    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    private Redisson redisson = null;
    private Config config = new Config();

    @PostConstruct
    private void init() {
        try {
            config.useSingleServer().setAddress("redis://" + host + ":" + port);
            log.info("redisson address {} {}", host, port);
            redisson = (Redisson) Redisson.create(config);
            log.info("Redisson 初始化完成");
        }
        catch (Exception e) {
            log.error("init Redisson error ", e);
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }
}