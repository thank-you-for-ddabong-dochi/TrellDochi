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

    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String rawMessage = new String(message.getBody());
            log.info("Received message: {}", rawMessage);

            // 메시지 파싱
            JsonNode rootNode = objectMapper.readTree(rawMessage);

            // sender 필드가 존재하는지 확인
            JsonNode senderNode = rootNode.get("sender");
            JsonNode messageNode = rootNode.get("message");

            if (senderNode == null || messageNode == null) {
                log.warn("메시지에 필요한 필드(sender 또는 message)가 없습니다. 메시지: {}", rawMessage);
                return;
            }

            String sender = senderNode.asText();
            String messageContent = messageNode.asText();

            // 발행자가 자신이면 메시지 무시
            if ("my-server".equals(sender)) {
                log.info("본인이 발행한 메시지입니다. 처리하지 않습니다.");
                return;
            }

            // Slack으로 메시지 전송
            notificationService.sendSlackNotification(slackWebhookUrl, messageContent);
        } catch (Exception e) {
            log.error("Error processing message: ", e);
        }
    }
}
