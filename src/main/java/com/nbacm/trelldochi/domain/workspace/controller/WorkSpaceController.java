package com.nbacm.trelldochi.domain.workspace.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.service.WorkSpaceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces")
public class WorkSpaceController {

    private final WorkSpaceServiceImpl workSpaceService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<WorkSpaceResponseDto>>> getWorkSpaces(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "조회 성공",
                        workSpaceService.getWorkSpaces(authUser.getEmail(), page, size)
                ));
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkSpaceResponseDto>> getWorkSpace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable(name = "workspaceId") Long workSpaceId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "조회 성공",
                        workSpaceService.getWorkSpace(authUser.getEmail(), workSpaceId)
                ));
    }



}
