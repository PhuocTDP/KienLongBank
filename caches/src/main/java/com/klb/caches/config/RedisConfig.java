package com.klb.caches.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class RedisConfig {

  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
  private int redisPort;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost,
        redisPort);
    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public CacheManager cacheManager() {
    RedisCacheConfiguration defaultCacheConfig = myDefaultCacheConfig(
        Duration.ofMinutes(1)).disableCachingNullValues();

    RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory())
        .cacheDefaults(defaultCacheConfig)
        .withCacheConfiguration("tutorials_all", myDefaultCacheConfig(Duration.ofMinutes(30)))
        .withCacheConfiguration("tutorials_by_title", myDefaultCacheConfig(Duration.ofMinutes(30)))
        .withCacheConfiguration("tutorial_by_id", myDefaultCacheConfig(Duration.ofMinutes(30)))
        .withCacheConfiguration("published_tutorials", myDefaultCacheConfig(Duration.ofMinutes(30)))
        .build();

    // Optional: Wrap RedisCacheManager with LoggingCache if needed
    return new LoggingCacheManager(cacheManager);
  }

  private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(duration)
        .serializeValuesWith(
            SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }
}
