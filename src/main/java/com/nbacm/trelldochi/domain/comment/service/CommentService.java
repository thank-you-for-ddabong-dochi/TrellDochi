package com.nbacm.trelldochi.domain.comment.service;

import com.nbacm.trelldochi.domain.comment.dot.CommentRequestDto;
import com.nbacm.trelldochi.domain.comment.dto.CommentResponseDto;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import org.springframework.stereotype.Service;


@Service
public interface CommentService {

    CommentResponseDto createCard(CustomUserDetails customUserDetails, Long cardId, CommentRequestDto commentRequestDto);

    CommentResponseDto putComment(CustomUserDetails customUserDetails, Long commentId, CommentRequestDto commentRequestDto);

    void deleteComment(CustomUserDetails customUserDetails, Long commentId);
}
