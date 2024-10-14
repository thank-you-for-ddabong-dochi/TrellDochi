package com.nbacm.trelldochi.domain.board.service;

import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 보드 생성
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, Long workspace_id) {
        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getContents());
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }


    public
}
