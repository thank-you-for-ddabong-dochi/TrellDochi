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
    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto boardRequestDto, CustomUserDetails userDetails) {

        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(workspaceId, user.getId()).orElseThrow();
        if(workSpaceMemberRepository.findByUserIdAndWorkspaceId(workspaceId, user.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 또한 맴버라면 해당 권한이 READONLY 가 아닌지 판단이 필요함
        if(workSpaceMember.getRole() == MemberRole.READONLY) {
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
    public BoardResponseDto updateBoard(Long workspaceId, Long boardId, BoardRequestDto boardRequestDto, CustomUserDetails userDetails) {

        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(workspaceId, user.getId()).orElseThrow();
        if(workSpaceMemberRepository.findByUserIdAndWorkspaceId(workspaceId, user.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 또한 맴버라면 해당 권한이 READONLY 가 아닌지 판단이 필요함
        if(workSpaceMember.getRole() == MemberRole.READONLY) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Board board = boardRepository.findById(boardId).orElseThrow();
        board.update(boardRequestDto);

        return new BoardResponseDto(board);
    }

    @Override
    public BoardResponseDto deleteBoard(Long workspaceId, Long boardId, CustomUserDetails userDetails) {

        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        // 현재 로그인 중인 유저가 해당 워크스페이스의 맴버인지 판단이 필요함
        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(workspaceId, user.getId()).orElseThrow();
        if(workSpaceMemberRepository.findByUserIdAndWorkspaceId(workspaceId, user.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 또한 맴버라면 해당 권한이 READONLY 가 아닌지 판단이 필요함
        if(workSpaceMember.getRole() == MemberRole.READONLY) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Board board = boardRepository.findById(boardId).orElseThrow();
        boardRepository.delete(board);

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
