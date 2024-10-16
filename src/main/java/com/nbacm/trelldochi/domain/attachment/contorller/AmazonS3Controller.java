package com.nbacm.trelldochi.domain.attachment.contorller;

import com.nbacm.trelldochi.domain.attachment.service.AwsS3Service;
import com.nbacm.trelldochi.domain.common.advice.ApiResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> s3Upload(@RequestPart(name = "image", required = false) MultipartFile multipartFile){
        String profileImage = awsS3Service.upload(multipartFile);
        return ResponseEntity.ok(profileImage);
    }

    @GetMapping("/delete")
    public ResponseEntity<?> s3delete(@RequestParam String addr){
        awsS3Service.deleteImageFromS3(addr);
        return ResponseEntity.ok(null);
    }

}

