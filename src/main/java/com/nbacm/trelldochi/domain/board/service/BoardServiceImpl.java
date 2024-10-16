package com.nbacm.trelldochi.domain.board.service;

import com.nbacm.trelldochi.domain.attachment.service.AwsS3Service;
import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.board.dto.BoardResponseDto;
import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.board.repository.BoardQueryDslRepositoryImpl;
import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.common.exception.NotFoundException;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final UserRepository userRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;
    private final BoardQueryDslRepositoryImpl boardQueryDslRepositoryImpl;
    private final AwsS3Service awsS3Service;
    private final NotificationService notificationService;


    @Override
    @Transactional
    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto boardRequestDto, MultipartFile image, CustomUserDetails userDetails) {

        if(!isAuthorized(workspaceId, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if(image != null) {
            String profileImage = awsS3Service.upload(image);
            boardRequestDto.addImageUrl(profileImage);
        }

        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Work Space Not Found"));

        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getBackgroundColor(), boardRequestDto.getBackgroundImageUrl(), workSpace);

        Board savedBoard = boardRepository.save(board);

        notificationService.sendRealTimeNotification("보드 생성",board.getTitle().toString()+"보드가 생성이 되었어요!");
        return new BoardResponseDto(savedBoard);
    }

    @Override
    public List<BoardResponseDto> getAllBoard(Long workspaceId) {

        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow(() -> new NotFoundException("Work Space Not Found"));

        List<Board> boardList = boardRepository.findByWorkSpace(workSpace);

        return boardList.stream().map(BoardResponseDto::new).toList();
    }

    @Override
    public BoardResponseDto getBoard(Long workspaceId, Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("Board Not Found"));

        if(board.getIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto updateBoard(Long workspaceId, Long boardId, BoardRequestDto boardRequestDto, MultipartFile image, CustomUserDetails userDetails) {

        if(!isAuthorized(workspaceId, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if(image != null) {
            String profileImage = awsS3Service.upload(image);
            boardRequestDto.addImageUrl(profileImage);
        }

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("Board Not Found"));
        board.update(boardRequestDto);
        notificationService.sendRealTimeNotification("보드 수정",board.getTitle().toString()+"보드가 수정이 되었어요!");

        return new BoardResponseDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto deleteBoard(Long workspaceId, Long boardId, CustomUserDetails userDetails) {

        if(!isAuthorized(workspaceId, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("Board Not Found"));

        if(board.getIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        boardQueryDslRepositoryImpl.deleteRelations(board.getId());

        return new BoardResponseDto(board);
    }

    private Boolean isAuthorized(Long workspaceId, CustomUserDetails userDetails) {
        User user = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId).orElseThrow(() -> new NotFoundException("Work Space Member Not Found"));

        if(workSpaceMember.getRole() == MemberRole.READONLY) {
            return false;
        } else {
            return true;
        }
    }

}
