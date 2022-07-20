package com.soulcode.servicos.Config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class CacheConfig {
    private final RedisSerializationContext.SerializationPair<Object> serializationPair =
            RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

    @Bean
    public RedisCacheConfiguration cacheConfiguration() { // customizar a config padrão do redis cache
        return RedisCacheConfiguration
                .defaultCacheConfig() // customizar os dados padrão do cache
                .entryTtl(Duration.ofMinutes(5)) // todos os caches terão 5 min por padrão
                .disableCachingNullValues() // converte do redis para json e vice-versa
                .serializeValuesWith(serializationPair);
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder.withCacheConfiguration("clientesCache",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(30))
                        .serializeValuesWith(serializationPair)
        ).withCacheConfiguration("chamadosCache",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(30))
                        .serializeValuesWith(serializationPair)
        ).withCacheConfiguration("userCache",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(5))
                        .serializeValuesWith(serializationPair)
        ).withCacheConfiguration("userDetailCache",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(60))
                        .serializeValuesWith(serializationPair)
        );
    }
}