package com.ajouton_2.server.api.service;

import com.ajouton_2.server.api.dto.file.PresignedUrlResponse;
import com.ajouton_2.server.domain.file.File;
import com.ajouton_2.server.domain.file.FileJpaRepository;
import com.ajouton_2.server.domain.post.Post;
import com.ajouton_2.server.domain.post.PostJpaRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final PostJpaRepository postRepository;
    private final FileJpaRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String createPresignedUrl(Long postId, String fileName) {
        if(fileName == null) {
            return null;
        }

        String fileKey = "aloda/" + postId + "/" +  UUID.randomUUID() + "_" + fileName;
        PutObjectPresignRequest request = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 10분 유효
                .putObjectRequest(builder -> builder
                        .bucket(bucketName)
                        .key(fileKey)
                        .build())
                .build();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 파일 저장
        File file = File.builder()
                .post(post)
                .fileName(fileName)
                .fileUrl("https://onrank-file-dev-bucket.s3.ap-northeast-2.amazonaws.com/" + fileKey)
                .createdAt(LocalDateTime.now())
                .build();
            fileRepository.save(file);

        return s3Presigner.presignPutObject(request).url().toString();
    }

    @Transactional
    public List<PresignedUrlResponse> createMultiplePresignedUrls(Long postId, List<String> fileNames){
        if(fileNames == null || fileNames.isEmpty()) return List.of();

        return fileNames.stream()
                .map(fileName -> new PresignedUrlResponse(fileName, createPresignedUrl(postId, fileName)))
                .toList();
    }
}
