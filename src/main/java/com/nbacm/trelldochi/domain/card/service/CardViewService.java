package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.CardResponseDto;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CardViewService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private CardRepository cardRepository;

    // 조회수 증가 기능
    public void incrementCardViewCountWithCache(Long cardId) {
        String key = "card_view_count" + cardId; // Redis Key
        redisTemplate.opsForValue().increment(key); // 해당 카드의 조회수를 1 증가시킴
    }

    // 조회수 조회 기능
    public int getCardViewCountWithCache(Long cardId) {
        String key = "card_view_count" + cardId;
        Object count = redisTemplate.opsForValue().get(key); // Redis 에서 조회수 가져오기
        return count != null ? Integer.parseInt(count.toString()) : 0; // 조회수가 없으면 0 반환
    }

    // 매일 자정에 조회수 초기화
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCardViewCountWithCache() {

        // Redis에서 "card:view:"로 시작하는 모든 키 가져오기
        Set<String> keys = redisTemplate.keys("card_view_count*");

        if(keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys); // 해당 키들을 Redis 에서 삭제 (조회수 초기화)
        }
    }

    // Cache 없이 viewCount 설정
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CardResponseDto incrementCardViewCount(Card card) {

        // 카드 조회수 증가
        card.addViewCount(card.getViewCount() + 1);
        cardRepository.save(card);

        // 카드 정보 반환
        return new CardResponseDto(card);
    }
}
