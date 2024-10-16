package com.nbacm.trelldochi.domain.common.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.Message;


@Component
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    @Value("${slack.webhook-url}")
    String slackWebhookUrl;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public RedisMessageSubscriber(NotificationService notificationService) {
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
            try {
                String notification = new String(message.getBody());
                log.info("Received message: {}", notification);
                notificationService.sendSlackNotification(
                        slackWebhookUrl,
                        notification  // Pass the raw JSON message
                );
            } catch (Exception e) {
                log.error("Error processing message: ", e);
            }

    }
}
