package com.nbacm.trelldochi.domain.comment.service;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.exception.CardNotFoundException;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.comment.dot.CommentRequestDto;
import com.nbacm.trelldochi.domain.comment.dto.CommentResponseDto;
import com.nbacm.trelldochi.domain.comment.entity.Comment;
import com.nbacm.trelldochi.domain.comment.exception.CommentNotFoundException;
import com.nbacm.trelldochi.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public CommentResponseDto createCard(Long cardId, CommentRequestDto commentRequestDto) {
        Card findCard = cardRepository.findById(cardId).orElseThrow(CardNotFoundException::new);
        Comment saveComment = commentRepository.save(new Comment(findCard, commentRequestDto));
        return new CommentResponseDto(saveComment);
    }

    @Transactional
    public CommentResponseDto putComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (findComment.isDeleted()) {
            throw new CommentNotFoundException();
        }
        Comment patchComment = findComment.putComment(commentRequestDto);
        return new CommentResponseDto(patchComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (findComment.isDeleted()) {
            throw new CommentNotFoundException();
        }
        findComment.deleteComment();
    }
}
