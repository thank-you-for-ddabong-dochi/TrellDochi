package com.nbacm.trelldochi.domain.attachment.contorller;

import com.nbacm.trelldochi.domain.attachment.dto.AttachmentRequestDto;
import com.nbacm.trelldochi.domain.attachment.entity.Attachment;
import com.nbacm.trelldochi.domain.attachment.service.AwsS3Service;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspace/")
public class AmazonS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping(value = "/bord/todo/cards/{cardId}/fileupload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> s3Upload(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("cardId") Long cardId,
            @RequestPart(name = "image", required = false) MultipartFile multipartFile,
            @RequestPart(name = "dto") AttachmentRequestDto attachmentRequestDto
            ){
        String profileImage = awsS3Service.upload(userDetails, cardId, multipartFile, attachmentRequestDto);
        return ResponseEntity.ok(profileImage);
    }

    @GetMapping("/bord/todo/cards/{cardId}/delete/{attachmentId}")
    public ResponseEntity<?> s3delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("cardId") Long cardId,
            @PathVariable("attachmentId") Long attachmentId){
        awsS3Service.deleteImageFromS3(userDetails, cardId, attachmentId);
        return ResponseEntity.ok(null);
    }

}

