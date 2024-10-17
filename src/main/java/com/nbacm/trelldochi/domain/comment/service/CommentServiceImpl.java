package com.nbacm.trelldochi.domain.comment.service;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.exception.CardNotFoundException;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.comment.dto.CommentRequestDto;
import com.nbacm.trelldochi.domain.comment.dto.CommentResponseDto;
import com.nbacm.trelldochi.domain.comment.entity.Comment;
import com.nbacm.trelldochi.domain.comment.exception.CommentForbiddenException;
import com.nbacm.trelldochi.domain.comment.exception.CommentNotFoundException;
import com.nbacm.trelldochi.domain.comment.repository.CommentRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public CommentResponseDto createCard(CustomUserDetails customUserDetails, Long cardId, CommentRequestDto commentRequestDto) {
        Card findCard = cardRepository.findById(cardId).orElseThrow(CardNotFoundException::new);
        Comment saveComment = commentRepository.save(new Comment(customUserDetails.getEmail(), findCard, commentRequestDto));
        notificationService.sendRealTimeNotification("댓글 작성",findCard.getTitle().toString()+
                "카드에 댓글이 작성 되었어요!");
        return new CommentResponseDto(saveComment);
    }

    @Override
    @Transactional
    public CommentResponseDto putComment(CustomUserDetails customUserDetails, Long commentId, CommentRequestDto commentRequestDto) {
        Comment findComment = isDeleted(commentId);

        if (customUserDetails.getEmail().equals(findComment.getUserEmail())) {
            Comment patchComment = findComment.putComment(commentRequestDto);
            return new CommentResponseDto(patchComment);
        } else {
            throw new CommentForbiddenException();
        }
    }

    @Override
    @Transactional
    public void deleteComment(CustomUserDetails customUserDetails, Long commentId) {
        Comment findComment = isDeleted(commentId);

        if (customUserDetails.getEmail().equals(findComment.getUserEmail())) {
            findComment.deleteComment();
        } else {
            throw new CommentNotFoundException();
        }

    }

    private Comment isDeleted(Long commentId){
        Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (findComment.isDeleted()) {
            throw new CommentNotFoundException();
        }
        return findComment;
    }
}
