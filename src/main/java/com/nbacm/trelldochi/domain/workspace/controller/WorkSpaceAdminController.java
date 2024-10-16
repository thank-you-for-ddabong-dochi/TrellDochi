package com.nbacm.trelldochi.domain.workspace.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceMemberResponseDto;
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

    private final WorkSpaceAdminServiceImpl workSpaceAdminService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkSpaceResponseDto>> createWorkSpace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody WorkSpaceRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "워크 스페이스 생성 성공",
                        workSpaceAdminService.createWorkSpace(authUser.getEmail(), requestDto)
                ));
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkSpaceResponseDto>> updateWorkSpace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody WorkSpaceRequestDto requestDto,
            @PathVariable Long workspaceId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "워크 스페이스 수정 성공",
                        workSpaceAdminService.updateWorkSpace(authUser.getEmail(), requestDto, workspaceId)
                ));
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<Long>> deleteWorkSpace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable(name = "workspaceId") Long workspaceId
    ) {
        workSpaceAdminService.deleteWorkSpace(authUser.getEmail(), workspaceId);
        // 필요시 반환값 추가
        return ResponseEntity.ok(
                ApiResponse.success(
                        "워크 스페이스 삭제 성공"
                ));
    }

    @PutMapping("/{workspaceId}/members/{memberId}")
    public ResponseEntity<ApiResponse<WorkSpaceMemberResponseDto>> changeMemberRole(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @PathVariable Long memberId,
            @RequestParam String role
    ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "멤버 수정 성공",
                        workSpaceAdminService.changeMemberRole(authUser.getEmail() ,workspaceId,memberId,role)
                )
        );
    }

    @PatchMapping("/{workspaceId}/members/{memberId}")
    public ResponseEntity<ApiResponse<?>> deleteMember(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @PathVariable Long memberId
    ){
        workSpaceAdminService.deleteMember(authUser.getEmail(), workspaceId,memberId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "멤버 삭제 성공"
                )
        );
    }

}
