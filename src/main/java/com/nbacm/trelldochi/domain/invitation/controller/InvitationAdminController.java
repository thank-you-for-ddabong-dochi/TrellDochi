package com.nbacm.trelldochi.domain.invitation.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationRequestDto;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationResponseDto;
import com.nbacm.trelldochi.domain.invitation.service.InvitationAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/workspaces")
public class InvitationAdminController {

    private final InvitationAdminServiceImpl invitationAdminService;
    @PostMapping("/{workspaceId}/invitations")
    public ResponseEntity<ApiResponse<InvitationResponseDto>> inviteMember(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @RequestBody InvitationRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "초대 전송",
                        invitationAdminService.inviteMember(
                                authUser.getEmail(),
                                workspaceId,
                                requestDto
                        ))
        );
    }

    @PostMapping("/{workspaceId}/invitations/{invitationId}/cancle")
    public ResponseEntity<ApiResponse<String>> cancleInvitation(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long invitationId
    ) {
        invitationAdminService.cancleInvitation(authUser.getEmail(), invitationId);
        return ResponseEntity.ok(ApiResponse.success(
                "CANCLE SUCCESS"
        ));
    }
}
