package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.repository.CardManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardManagerServiceImpl implements CardManagerService{

}
