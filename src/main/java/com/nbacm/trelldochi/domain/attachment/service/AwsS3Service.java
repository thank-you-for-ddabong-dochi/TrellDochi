package com.nbacm.trelldochi.domain.attachment.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.nbacm.trelldochi.domain.attachment.dto.AttachmentRequestDto;
import com.nbacm.trelldochi.domain.attachment.entity.Attachment;
import com.nbacm.trelldochi.domain.attachment.exception.AttachmentNotFoundException;
import com.nbacm.trelldochi.domain.attachment.repository.AttachmentRepository;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import com.nbacm.trelldochi.domain.card.exception.CardForbiddenException;
import com.nbacm.trelldochi.domain.card.exception.CardNotFoundException;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String upload(CustomUserDetails userDetails, Long cardId, MultipartFile image, AttachmentRequestDto attachmentRequestDto) {
        // 권한 확인 후 card 찾기
        Card findCard = findCard(userDetails, cardId);

        // 이미지 파일이 없는 경우 오류
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw new AmazonS3Exception("not found image");
        }

        // 이미지 저장하기
        String url = this.uploadImage(image);
        // attachment에 저장하기
        attachmentRepository.save(new Attachment(attachmentRequestDto.getFileName(), url, findCard));

        return url;
    }

    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new AmazonS3Exception("could not upload image", e);
        }
    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new AmazonS3Exception("not found image");
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new AmazonS3Exception("유효하지 않은 파일입니다.");
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        }catch (Exception e){
            throw new AmazonS3Exception("유효하지 않습니다.");
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3(CustomUserDetails userDetails, Long cardId, Long attachmentId){
        // 권한 확인
        findCard(userDetails, cardId);

        Attachment findAttachment = attachmentRepository.findById(attachmentId).orElseThrow(AttachmentNotFoundException::new);

        String key = getKeyFromImageAddress(findAttachment.getUrl());
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new AmazonS3Exception("삭제하지 못했습니다.");
        }

        attachmentRepository.deleteById(attachmentId);
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new AmazonS3Exception("이미지가 없습니다.");
        }
    }

    private Card findCard(CustomUserDetails userDetails, Long cardId){
        // 카드 찾기
        Card findCard = cardRepository.findCardAndCommentsById(cardId).orElseThrow(CardNotFoundException::new);

        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }

        // 카드 담당자인지 찾습니다.
        User findUser = userRepository.findByEmailOrElseThrow(userDetails.getEmail());
        CardManager findCardManager = cardRepository.findUserInUserList(cardId, findUser.getId()).orElseThrow(CardForbiddenException::new);

        return findCard;
    }
}

