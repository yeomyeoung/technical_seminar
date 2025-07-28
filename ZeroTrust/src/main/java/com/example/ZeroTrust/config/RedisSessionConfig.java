import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(
        @Value("${spring.redis.host}") String host,
        @Value("${spring.redis.port}") int port
    ) {
        System.out.println("🔧 [DEBUG] RedisConnectionFactory 생성됨: " + host + ":" + port);
        return new LettuceConnectionFactory(host, port);
    }
}