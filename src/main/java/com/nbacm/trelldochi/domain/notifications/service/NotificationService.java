package com.nbacm.trelldochi.domain.notifications.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;


    public void sendRealTimeNotification(String eventType,String message) {

        try {
            // Redis에 알림 발행
            log.info("실시간 알림 전송 중: {} - {}", eventType, message);
            redisTemplate.convertAndSend("notifications." + eventType, message);
            log.info("Redis 채널로 실시간 알림 전송 완료: {}", eventType);


        } catch (Exception e) {
            log.error("실시간 알림 전송 중 오류 발생: ", e);
        }
    }

    public void sendSlackNotification(String webhookUrl, String jsonMessage) {
        try {
            log.info("Slack에 대한 메시지를 받았습니다: {}", jsonMessage);

            String message = extractMessage(jsonMessage);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = new HashMap<>();
            payload.put("text", message);

            String jsonPayload = objectMapper.writeValueAsString(payload);

            log.debug("Slack으로 페이로드: {}", jsonPayload);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            String response = restTemplate.postForObject(webhookUrl, request, String.class);
            log.info("슬랙 알림이 성공적으로 전송 되었습니다. Response: {}", response);
        } catch (Exception e) {
            log.error("Slack 알림 전송 오류: ", e);
        }
    }

    private String extractMessage(String jsonMessage) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonMessage);
            if (rootNode.isObject()) {
                for (String fieldName : new String[]{"message", "text", "content"}) {
                    JsonNode node = rootNode.get(fieldName);
                    if (node != null && node.isTextual()) {
                        return node.asText();
                    }
                }
                return rootNode.toString();
            } else if (rootNode.isTextual()) {
                return rootNode.asText();
            } else {
                return rootNode.toString();
            }
        } catch (Exception e) {
            log.error("Error parsing JSON message: ", e);
            return jsonMessage;
        }
    }
}


