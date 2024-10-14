package com.nbacm.trelldochi.domain.board.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {

    private String title;
    private String contents;

    public BoardRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
