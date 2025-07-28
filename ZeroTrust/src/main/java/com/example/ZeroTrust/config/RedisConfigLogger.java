package com.example.ZeroTrust.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RedisConfigLogger implements CommandLineRunner {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.session.store-type}")
    private String sessionType;

    @Override
    public void run(String... args) {
        System.out.println("🔧 [DEBUG] Redis 설정 확인:");
        System.out.println("🔧 Redis Host: " + redisHost);
        System.out.println("🔧 Redis Port: " + redisPort);
        System.out.println("🔧 Session Store Type: " + sessionType);
    }
}
