package com.devikone.service;

import com.devikone.transports.Redis;

import java.util.Map;

import javax.inject.Singleton;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

@Singleton
public class IBRedisProducer {

    private String redisHost = "localhost";
    private String redisPassword = "";

    @Produces
    @ApplicationScoped
    @Named("redis")
    public Redis IBRedisProducer() {
        Redis redis = new Redis();
        Map<String, String> env = System.getenv();
        if (env.get("REDIS_HOST") != null) {
            redisHost = env.get("REDIS_HOST");
        }
        if (env.get("REDIS_PASSWORD") != null) {
            redisPassword = env.get("REDIS_PASSWORD");
        }
        //System.out.println("REDIS HOST = " + redisHost);
        redis.setRedisHost(redisHost);
        redis.setRedisPassword(redisPassword);
        return redis;
    }
}