package insper.collie.squad.config;

import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import lombok.Getter;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class RedisConfig {
    
    @Value("${spring.redis.host}")
    private String host;
    
    @Value("${spring.redis.port}")
    private int port;
    

    // referência: https://blog.devgenius.io/redis-spring-boot-docker-compose-722ea61d68e9
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        // configura a conexão com o Redis com o host e porta fornecidos
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // configura o template do Redis e os serializadores para as chaves e os valores
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        // template.afterPropertiesSet();
        
        return template;
    }

    // referência https://dev.to/jackynote/a-guide-to-using-redis-in-spring-boot-custom-cachemanager-4e5k
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Configura o gerenciador de cache do Redis
        return RedisCacheManagerBuilder
            .fromConnectionFactory(connectionFactory)
            .withCacheConfiguration("squads",
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(10)) // TTL set to 10 minutes
            )
            .build();
    }
}