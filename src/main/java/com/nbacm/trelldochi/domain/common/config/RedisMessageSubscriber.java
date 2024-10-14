package com.nbacm.trelldochi.domain.common.config;

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
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String notification = new String(message.getBody());
            log.info("Received message: {}", notification);
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            notificationService.sendSlackNotification(
                    slackWebhookUrl,
                    notification  // Pass the raw JSON message
            );
        } catch (Exception e) {
            log.error("Error processing message: ", e);
        }
    }

}
