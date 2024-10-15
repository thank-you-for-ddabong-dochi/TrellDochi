package com.nbacm.trelldochi.domain.invitation.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.AuthUser;
import com.nbacm.trelldochi.domain.invitation.service.InvitationAdminServiceImpl;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationRequestDto;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InvitationAdminController {

    private final InvitationAdminServiceImpl invitationAdminService;
    @PostMapping("/{workspaceId}/invitations")
    public ResponseEntity<ApiResponse<InvitationResponseDto>> inviteMember(
            @AuthenticationPrincipal AuthUser authUser,
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
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long invitationId
    ) {
        invitationAdminService.cancleInvitation(authUser.getEmail(), invitationId);
        return ResponseEntity.ok(ApiResponse.success(
                "REJECT SUCCESS"
        ));
    }
}
