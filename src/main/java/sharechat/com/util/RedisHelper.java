package sharechat.com.util;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisHelper {
    private final RedisTemplate redisTemplate;

    public RedisHelper(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 存储一个键值对
     *
     * @param key 键
     * @param value 值
     * */
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
