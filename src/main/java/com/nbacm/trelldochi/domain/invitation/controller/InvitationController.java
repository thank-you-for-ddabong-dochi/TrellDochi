package com.nbacm.trelldochi.domain.invitation.controller;

import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.invitation.service.InvitationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces")
public class InvitationController {

    private final InvitationServiceImpl invitationService;

    @PostMapping("/{workspaceId}/invitations/accept")
    public ResponseEntity<ApiResponse<String>> accpetInvitation(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId
    ) {
        invitationService.acceptInvitation(authUser.getEmail(), workspaceId);
        return ResponseEntity.ok(ApiResponse.success(
                "ACCEPT SUCCESS"
        ));
    }

    @PostMapping("/{workspaceId}/invitations/reject")
    public ResponseEntity<ApiResponse<String>> rejectInvitation(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId
    ) {
        invitationService.rejectInvitation(authUser.getEmail(), workspaceId);
        return ResponseEntity.ok(ApiResponse.success(
                "REJECT SUCCESS"
        ));
    }
}
