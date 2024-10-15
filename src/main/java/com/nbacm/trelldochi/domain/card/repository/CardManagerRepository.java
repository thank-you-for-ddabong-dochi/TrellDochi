package com.nbacm.trelldochi.domain.card.repository;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardManagerRepository extends JpaRepository<CardManager, Long> {
}
