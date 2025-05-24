package com.ajouton_2.server.api.service;

import com.ajouton_2.server.api.dto.file.PresignedUrlResponse;
import com.ajouton_2.server.api.dto.post.PostRequest;
import com.ajouton_2.server.domain.file.FileJpaRepository;
import com.ajouton_2.server.domain.group.Group;
import com.ajouton_2.server.domain.group.GroupJpaRepository;
import com.ajouton_2.server.domain.groupmember.GroupMember;
import com.ajouton_2.server.domain.groupmember.GroupMemberJpaRepository;
import com.ajouton_2.server.domain.participant.Participant;
import com.ajouton_2.server.domain.participant.ParticipantJpaRepository;
import com.ajouton_2.server.domain.post.Post;
import com.ajouton_2.server.domain.post.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostJpaRepository postRepository;
    private final FileJpaRepository fileRepository;
    private final FileService fileService;
    private final ParticipantJpaRepository participantRepository;
    private final GroupJpaRepository groupRepository;
    private final GroupMemberJpaRepository groupMemberRepository;

    @Transactional
    public List<PresignedUrlResponse> createPost(Long groupId, PostRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Post post = Post.builder()
                .group(group)
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // 참여자 저장
        for (Long groupMemberId : request.getParticipantIds()) {
            GroupMember gm = groupMemberRepository.findById(groupMemberId)
                    .orElseThrow(() -> new IllegalArgumentException("GroupMember not found"));

            Participant participant = Participant.builder()
                    .groupMember(gm)
                    .post(post)
                    .build();
            participantRepository.save(participant);
        }

        // 참여자 저장
        List<PresignedUrlResponse> responses = fileService.createMultiplePresignedUrls(post.getPostId(), request.getFileNames());
        return responses;
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public List<PresignedUrlResponse> updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.update(request.getTitle(), request.getContent());

        // 기존 파일/참여자 삭제 후 재등록
        fileRepository.deleteAllByPost(post);
        participantRepository.deleteAllByPost(post);

        for (Long memberGroupId : request.getParticipantIds()) {
            GroupMember gm = groupMemberRepository.findById(memberGroupId)
                    .orElseThrow(() -> new IllegalArgumentException("GroupMember not found"));
            participantRepository.save(Participant.builder()
                    .groupMember(gm)
                    .post(post)
                    .build());
        }
        List<PresignedUrlResponse> responses = fileService.createMultiplePresignedUrls(post.getPostId(), request.getFileNames());
        return responses;
    }
}

