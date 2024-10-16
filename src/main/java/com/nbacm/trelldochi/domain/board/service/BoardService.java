package com.nbacm.trelldochi.domain.board.service;

import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    BoardResponseDto createBoard(Long workspaceId, BoardRequestDto boardRequestDto, MultipartFile image, CustomUserDetails userDetails);
    List<BoardResponseDto> getAllBoard(Long workspaceId);
    BoardResponseDto getBoard(Long workspaceId, Long boardId);
    BoardResponseDto updateBoard(Long workspaceId, Long boardId, BoardRequestDto boardRequestDto, MultipartFile image, CustomUserDetails userDetails);
    BoardResponseDto deleteBoard(Long workspaceId, Long boardId, CustomUserDetails userDetails);

}
