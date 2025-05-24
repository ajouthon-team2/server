package com.ajouton_2.server.api.dto.post;

import lombok.Getter;

import java.util.List;

@Getter
public class PostRequest {
    private String title;
    private String content;
    private List<String> fileNames; // 파일 이름만 받아서 Pre-signed URL로 처리
    private List<Long> participantIds;
}
