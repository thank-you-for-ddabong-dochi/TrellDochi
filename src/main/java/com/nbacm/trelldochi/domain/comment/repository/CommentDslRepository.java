package com.nbacm.trelldochi.domain.comment.repository;

import org.springframework.stereotype.Repository;

public interface CommentDslRepository {
    void deleteAllWithCardId(Long cardId);
}
