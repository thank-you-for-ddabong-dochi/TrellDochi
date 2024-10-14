package com.nbacm.trelldochi.domain.board.dto;

import com.nbacm.trelldochi.domain.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {

    private String title;
    private String contents;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
    }
}
