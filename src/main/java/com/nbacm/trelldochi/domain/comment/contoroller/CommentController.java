package com.nbacm.trelldochi.domain.comment.contoroller;

import com.nbacm.trelldochi.domain.comment.dto.CommentRequestDto;
import com.nbacm.trelldochi.domain.comment.dto.CommentResponseDto;
import com.nbacm.trelldochi.domain.comment.service.CommentService;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/card/{cardId}/comment")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("cardId") Long cardId,
            @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.createCard(userDetails, cardId, commentRequestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글 생성 성공", commentResponseDto));
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> patchComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.putComment(userDetails, commentId, commentRequestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글 수정 성공", commentResponseDto));
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse<Long>> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(userDetails, commentId);
        return ResponseEntity.ok(ApiResponse.success("댓글 삭제 성공", commentId));
    }
}
