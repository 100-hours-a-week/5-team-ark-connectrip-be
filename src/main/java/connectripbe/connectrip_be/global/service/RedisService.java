package connectripbe.connectrip_be.global.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Redis DB 데이터 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 주어진 Key 에 대응하는 데이터를 Redis DB 에서 조회
     *
     * @param key 조회하고자 하는 데이터의 Key
     * @return key 에 대응하는 데이터
     */
    public String getData(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get(key);
    }

    /**
     * 주어진 key 와 value 를 Redis DB에 저장. 저장된 데이터는 주어진 시간이 지나면 자동으로 삭제
     *
     * @param key         저장하고자하는 데이터의 key
     * @param value       저장하고자하는 데이터의 value
     * @param expiredTime 데이터의 만료 시간 (밀리초 단위)
     */
    public void setDataExpire(String key, String value, Long expiredTime) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, Duration.ofMillis(expiredTime));
    }

    /**
     * 주어진 key 에 대응하는 데이터가 Redis DB에 존재하는지 확인.
     *
     * @param key 확인하고자 하는 데이터의 key
     * @return 데이터 존재 여부
     */
    public boolean existData(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 주어진 key 에 대등하는 데이터를 Redis DB에 삭제.
     *
     * @param key 삭제하고자는 데이터의 key
     */
    public void deleteData(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(result)) {
                log.info("Successfully deleted key : {}", key);
            } else {
                log.info("Failed to delete key : {}", key);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting key : {}", key, e);
        }
    }

    // 해시 값을 Redis 에서 조회 (명확한 반환 타입을 받기 위해 Class<T> 사용)
    public <T> T getChatRoomHashKey(String hashKey, String key, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(hashKey, key);
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        throw new GlobalException(ErrorCode.REDIS_CAST_ERROR);
    }

    public <T> T getClassData(String hashKey, String key, Class<T> elementClass) {
        try {
            Object jsonResult = redisTemplate.opsForHash().get(hashKey, key);
            if (StringUtils.isEmpty(jsonResult)) {
                throw new GlobalException(ErrorCode.REDIS_CAST_ERROR);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                return elementClass.cast(jsonResult);
            }
        } catch (Exception e) {
            log.error("{} is occurred", e.getMessage());
            throw e;
        }
    }


    // Redis 에 해시 값 저장
    public void updateToHash(String hashKey, String key, Object value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(value);
            redisTemplate.opsForHash().put(hashKey, key, jsonData);

        } catch (Exception e) {
            log.error("Error occurred while updating hash : {}", e.getMessage());
        }
    }

    // Redis 에서 해시 값 삭제
    public void deleteHashKey(String hashKey, String key) {
        redisTemplate.opsForHash().delete(hashKey, key);
    }
}
