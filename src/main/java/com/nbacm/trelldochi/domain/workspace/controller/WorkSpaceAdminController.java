package com.nbacm.trelldochi.domain.workspace.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.AuthUser;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.service.WorkSpaceAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/workspaces")
public class WorkSpaceAdminController {

    private final WorkSpaceAdminServiceImpl workSpaceService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkSpaceResponseDto>> createWorkSpace(@AuthenticationPrincipal AuthUser authUser, WorkSpaceRequestDto requestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "워크 스페이스 생성 성공",
                        workSpaceService.createWorkSpace(authUser.getEmail(), requestDto)
                ));
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkSpaceResponseDto>> updateWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            WorkSpaceRequestDto requestDto,
            @PathVariable Long workspaceId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "워크 스페이스 수정 성공",
                        workSpaceService.updateWorkSpace(authUser.getEmail(), requestDto, workspaceId)
                ));
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<Long>> deleteWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            WorkSpaceRequestDto requestDto,
            @PathVariable Long workspaceId
    ) {
        workSpaceService.deleteWorkSpace(authUser.getEmail(), workspaceId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "워크 스페이스 삭제 성공"
                ));
    }
}
