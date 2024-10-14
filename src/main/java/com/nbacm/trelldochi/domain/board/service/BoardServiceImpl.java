package com.nbacm.trelldochi.domain.board.service;

import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final UserRepository userRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;


    @Override
    @Transactional
    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto boardRequestDto, CustomUserDetails userDetails) {

        if(!isAuthorized(workspaceId, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow();

        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getContents(), null, workSpace);

        Board savedBoard = boardRepository.save(board);

        return new BoardResponseDto(savedBoard);
    }

    @Override
    public List<BoardResponseDto> getAllBoard(Long workspaceId) {

        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow();

        List<Board> boardList = workSpace.getBoards();

        return boardList.stream().map(BoardResponseDto::new).toList();
    }

    @Override
    public BoardResponseDto getBoard(Long workspaceId, Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow();

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto updateBoard(Long workspaceId, Long boardId, BoardRequestDto boardRequestDto, CustomUserDetails userDetails) {

        if(!isAuthorized(workspaceId, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Board board = boardRepository.findById(boardId).orElseThrow();
        board.update(boardRequestDto);

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto deleteBoard(Long workspaceId, Long boardId, CustomUserDetails userDetails) {

        if(!isAuthorized(workspaceId, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Board board = boardRepository.findById(boardId).orElseThrow();
        board.delete();

        return new BoardResponseDto(board);
    }

    private Boolean isAuthorized(Long workspaceId, CustomUserDetails userDetails) {
        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId).orElseThrow();

        if(workSpaceMember.getRole() == MemberRole.READONLY) {
            return false;
        } else {
            return true;
        }
    }

}
