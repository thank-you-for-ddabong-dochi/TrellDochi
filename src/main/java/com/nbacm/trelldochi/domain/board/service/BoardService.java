package com.nbacm.trelldochi.domain.board.service;

import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;

import java.util.List;

public interface BoardService {

    BoardResponseDto createBoard(Long workspaceId, BoardRequestDto boardRequestDto);
    List<BoardResponseDto> getAllBoard(Long workspaceId);
    BoardResponseDto getBoard(Long workspaceId, Long boardId);
    BoardResponseDto updateBoard(Long workspaceId, Long boardId, BoardRequestDto boardRequestDto);
    BoardResponseDto deleteBoard(Long workspaceId, Long boardId);

}
