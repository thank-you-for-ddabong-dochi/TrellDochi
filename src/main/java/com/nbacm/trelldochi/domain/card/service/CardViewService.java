package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.CardOneResponseDto;
import com.nbacm.trelldochi.domain.card.dto.CardRankingResponseDto;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardViewService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CardRepository cardRepository;

    private static final String VIEW_COUNT_KEY = "card:viewCount:";
    private static final String RANKING_KEY = "card:ranking";

    // 조회수 증가
    public void incrementCardViewCountWithCache(Long cardId, Long userId) {

        String cardKey = VIEW_COUNT_KEY + cardId;
        String userKey = VIEW_COUNT_KEY + cardId + "userId:" + userId;

        // 사용자가 이미 조회한 사용자인지 확인
        if(Boolean.FALSE.equals(redisTemplate.hasKey(userKey))) {
            // 캐시에서 조회수 가져오기
            Integer viewCount = getCardViewCountWithCache(cardId);

            if (viewCount == null) {
                // 캐시에 없으면 DB에서 조회
                viewCount = cardRepository.findById(cardId).orElseThrow().getViewCount();
                redisTemplate.opsForValue().set(cardKey, viewCount);
            }

            // 조회수 증가, increment를 사용해서 해당 키에 대한 값을 증가시킴
            redisTemplate.opsForValue().increment(cardKey);

            // 사용자 조회 이력 저장, set을 사용해서 해당 키에 해당하는 값을 저장함
            redisTemplate.opsForValue().set(userKey, true, 24*60*60, TimeUnit.SECONDS);

            // 랭킹 갱신, incrementScore를 사용해서 주어진 ZSet에서 특정 값의 점수를 증가시킴
            redisTemplate.opsForZSet().incrementScore(RANKING_KEY, cardId.toString(), 1);
        }
    }

    // 조회수 랭킹 가져오기
    public List<CardRankingResponseDto> getRanking(int topN) {
        // 상위 topN개의 카드의 ID를 반환, reverseRange를 사용해서 점수가 높은 순서로 요소를 반환
        Set<Object> rankedCardIds = redisTemplate.opsForZSet().reverseRange(RANKING_KEY, 0, topN -1);

        // 카드 ID가 존재할 경우, DB에서 조회
        if (rankedCardIds != null && !rankedCardIds.isEmpty()) {
            List<Long> cardIdList = rankedCardIds.stream()
                    .map(Object::toString)
                    .map(Long::valueOf)
                    .toList();

            // CardRankingResponseDto로 변환 (순위 및 조회수 포함)
            List<Card> rankedCards = cardRepository.findAllById(cardIdList);

            List<CardRankingResponseDto> cardRanking = new ArrayList<>();
            int rank = 1;
            for (Card card : rankedCards) {
                int viewCount = getCardViewCountWithCache(card.getId());
                cardRanking.add(new CardRankingResponseDto(card.getId(), card.getTitle(), rank++, viewCount));
            }
            return cardRanking;
        }

        // 랭킹에 해당하는 카드가 없을 경우 빈 리스트 반환
        return Collections.emptyList();
    }

    // 조회수 조회 기능
    public int getCardViewCountWithCache(Long cardId) {
        String cardKey = VIEW_COUNT_KEY + cardId;
        Object count = redisTemplate.opsForValue().get(cardKey); // Redis 에서 조회수 가져오기
        return count != null ? Integer.parseInt(count.toString()) : 0; // 조회수가 없으면 0 반환
    }

    // 매일 자정에 조회수 초기화
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCardViewCount() {

        // Redis에서 "card:viewCount:"로 시작하는 모든 키 가져오기
        Set<String> keys = redisTemplate.keys(VIEW_COUNT_KEY+"*");

        if(keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys); // 해당 키들을 Redis 에서 삭제 (조회수 초기화)
        }
        redisTemplate.delete(RANKING_KEY);
    }

    // Cache 없이 viewCount 설정
    public CardResponseDto incrementCardViewCount(Card card) {

        // 카드 조회수 증가
        card.addViewCount(card.getViewCount() + 1);
        cardRepository.save(card);

        // 카드 정보 반환
        return new CardResponseDto(card);
    }
}
