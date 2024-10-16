package com.nbacm.trelldochi.domain.common.config;

import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.Message;


@Component
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    @Value("${slack.webhook-url}")
    String slackWebhookUrl;
    private final NotificationService notificationService;
    private final RedisMessageDuplicator deduplicator;


    public RedisMessageSubscriber(NotificationService notificationService, RedisMessageDuplicator deduplicator) {
        this.notificationService = notificationService;
        this.deduplicator =deduplicator;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String notification = new String(message.getBody());
            log.info("수신된 메시지: {}", notification);

            if (deduplicator.isNewMessage(notification)) {
                notificationService.sendSlackNotification(slackWebhookUrl, notification);
                log.info("Slack으로 알림 전송 완료: {}", notification);
            } else {
                log.info("중복 메시지 감지, 무시됨: {}", notification);
            }
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: ", e);
        }
    }
}
