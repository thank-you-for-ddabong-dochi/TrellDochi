package com.nbacm.trelldochi.domain.comment.service;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.exception.CardNotFoundException;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.comment.dot.CommentRequestDto;
import com.nbacm.trelldochi.domain.comment.dto.CommentResponseDto;
import com.nbacm.trelldochi.domain.comment.entity.Comment;
import com.nbacm.trelldochi.domain.comment.exception.CommentForbiddenException;
import com.nbacm.trelldochi.domain.comment.exception.CommentNotFoundException;
import com.nbacm.trelldochi.domain.comment.repository.CommentRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public CommentResponseDto createCard(CustomUserDetails customUserDetails, Long cardId, CommentRequestDto commentRequestDto) {
        Card findCard = cardRepository.findById(cardId).orElseThrow(CardNotFoundException::new);
        Comment saveComment = commentRepository.save(new Comment(customUserDetails.getEmail(), findCard, commentRequestDto));
        return new CommentResponseDto(saveComment);
    }

    @Transactional
    public CommentResponseDto putComment(CustomUserDetails customUserDetails, Long commentId, CommentRequestDto commentRequestDto) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (findComment.isDeleted()) {
            throw new CommentNotFoundException();
        }

        if (customUserDetails.getEmail().equals(findComment.getUserEmail())) {
            Comment patchComment = findComment.putComment(commentRequestDto);
            return new CommentResponseDto(patchComment);
        } else {
            throw new CommentForbiddenException();
        }
    }

    @Transactional
    public void deleteComment(CustomUserDetails customUserDetails, Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (findComment.isDeleted()) {
            throw new CommentNotFoundException();
        }

        if (customUserDetails.getEmail().equals(findComment.getUserEmail())) {
            findComment.deleteComment();
        } else {
            throw new CommentNotFoundException();
        }

    }
}
