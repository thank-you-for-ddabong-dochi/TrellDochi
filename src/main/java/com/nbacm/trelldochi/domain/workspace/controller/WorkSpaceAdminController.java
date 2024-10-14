package com.nbacm.trelldochi.domain.workspace.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.AuthUser;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.service.WorkSpaceAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
