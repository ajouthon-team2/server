package com.ajouton_2.server.api.controller;

import com.ajouton_2.server.api.dto.file.PresignedUrlResponse;
import com.ajouton_2.server.api.dto.post.PostRequest;
import com.ajouton_2.server.api.dto.post.PostResponse;
import com.ajouton_2.server.api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups/{groupId}/posts")
public class PostController {

    private final PostService postService;


    @PostMapping
    public ResponseEntity<List<PresignedUrlResponse>> createPost(
            @PathVariable Long groupId,
            @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.createPost(groupId, request));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<List<PresignedUrlResponse>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}

