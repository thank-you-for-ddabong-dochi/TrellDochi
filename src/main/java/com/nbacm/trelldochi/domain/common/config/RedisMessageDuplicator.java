package com.nbacm.trelldochi.domain.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Duration;

@Component

public class RedisMessageDuplicator {
    private final RedisTemplate<String, String> redisTemplate;
    private final Set<String> processedHashes = ConcurrentHashMap.newKeySet();

    public RedisMessageDuplicator(RedisTemplate<String, String> customStringRedisTemplate) {
        this.redisTemplate = customStringRedisTemplate;
    }
    public boolean isNewMessage(String message) {
        String messageHash = calculateHash(message);

        String key = "processed_message:" + messageHash;

        // Redis 명령어로 SETNX와 EXPIRE를 원자적으로 처리
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofMinutes(5));

        // isNew가 TRUE면 새로운 메시지, FALSE면 중복 메시지
        return Boolean.TRUE.equals(isNew);
    }

    private String calculateHash(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (Exception e) {
            throw new RuntimeException("메시지 해시 계산 중 오류 발생", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
