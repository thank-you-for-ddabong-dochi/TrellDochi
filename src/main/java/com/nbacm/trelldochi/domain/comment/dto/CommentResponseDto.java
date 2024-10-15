package com.nbacm.trelldochi.domain.comment.dto;

import com.nbacm.trelldochi.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String title;
    private String contents;

    public CommentResponseDto(Comment saveComment) {
        commentId = saveComment.getId();
        title = saveComment.getTitle();
        contents = saveComment.getContents();
    }
}
