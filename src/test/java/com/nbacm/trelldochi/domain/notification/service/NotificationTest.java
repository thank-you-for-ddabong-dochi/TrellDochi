package com.nbacm.trelldochi.domain.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendRealTimeNotification_Success() {
        // given
        String eventType = "testEvent";
        String message = "test message";

        // when
        notificationService.sendRealTimeNotification(eventType, message);

        // then
        verify(redisTemplate, times(1)).convertAndSend(eq("notifications." + eventType), eq(message));
    }

    @Test
    void sendSlackNotification_Success() throws Exception {
        // given
        String webhookUrl = "https://hooks.slack.com/services/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";
        String message = "Slack test message";

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", message);

        String jsonPayload = "{\"text\":\"Slack test message\"}";

        // mocking the objectMapper.writeValueAsString method
        when(objectMapper.writeValueAsString(any(Map.class))).thenReturn(jsonPayload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> expectedRequest = new HttpEntity<>(jsonPayload, headers);

        // when
        notificationService.sendSlackNotification(webhookUrl, message);

        // then
        verify(restTemplate, times(1)).postForObject(eq(webhookUrl), eq(expectedRequest), eq(String.class));
    }

    @Test
    void sendSlackNotification_Exception() throws Exception {
        // given
        String webhookUrl = "https://slack.com/webhook-url";
        String message = "Slack test message";

        String jsonPayload = "{\"text\":\"Slack test message\"}";

        // mocking the objectMapper.writeValueAsString method
        when(objectMapper.writeValueAsString(any(Map.class))).thenReturn(jsonPayload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> expectedRequest = new HttpEntity<>(jsonPayload, headers);

        // simulate an exception when calling Slack API
        doThrow(new RuntimeException("Slack API Error")).when(restTemplate).postForObject(eq(webhookUrl), eq(expectedRequest), eq(String.class));

        // when
        notificationService.sendSlackNotification(webhookUrl, message);

        // then
        verify(restTemplate, times(1)).postForObject(eq(webhookUrl), eq(expectedRequest), eq(String.class));
        // no exception should propagate; logging is expected
    }
}
