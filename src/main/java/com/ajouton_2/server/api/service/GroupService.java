package com.ajouton_2.server.api.service;

import com.ajouton_2.server.api.dto.group.GroupAddRequest;
import com.ajouton_2.server.api.dto.group.GroupInviteCodeResponse;
import com.ajouton_2.server.api.dto.group.GroupListResponse;
import com.ajouton_2.server.api.dto.groupmember.GroupMemberResponse;
import com.ajouton_2.server.api.dto.post.PostResponse;
import com.ajouton_2.server.domain.file.FileJpaRepository;
import com.ajouton_2.server.domain.file.File;
import com.ajouton_2.server.domain.group.Group;
import com.ajouton_2.server.domain.group.GroupJpaRepository;
import com.ajouton_2.server.domain.groupmember.GroupMember;
import com.ajouton_2.server.domain.groupmember.GroupMemberJpaRepository;
import com.ajouton_2.server.domain.member.Member;
import com.ajouton_2.server.domain.participant.ParticipantJpaRepository;
import com.ajouton_2.server.domain.post.Post;
import com.ajouton_2.server.domain.post.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupJpaRepository groupRepository;
    private final GroupMemberJpaRepository groupMemberRepository;
    private final MemberService memberService;
    private final PostJpaRepository postRepository;
    private final FileJpaRepository fileRepository;
    private final ParticipantJpaRepository participantRepository;

    @Transactional
    public GroupInviteCodeResponse createGroup(GroupAddRequest request) {
        String uniqueCode = generateCode();

        Group group = Group.builder()
                .name(request.getName())
                .category(request.getCategory())
                .inviteCode(uniqueCode)
                .build();

        groupRepository.save(group);

        Member member = memberService.getLoginedMember();

        GroupMember groupMember = GroupMember.builder()
                .group(group)
                .role(GroupMember.Role.LEADER)
                .member(member)
                .build();
        groupMemberRepository.save(groupMember);

        return GroupInviteCodeResponse.builder()
                .inviteCode(group.getInviteCode())
                .build();
    }

    private String generateCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(6);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public GroupInviteCodeResponse getInviteCode(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group Not Found"));

        return GroupInviteCodeResponse.builder()
                .inviteCode(group.getInviteCode()).
                build();
    }

    public List<GroupListResponse> getGroups() {
        Member member = memberService.getLoginedMember();

        // 해당 멤버가 속한 모든 그룹 멤버십 정보 조회
        List<GroupMember> groupMembers = groupMemberRepository.findByMemberMemberId(member.getMemberId());

        // DTO 변환
        return groupMembers.stream()
                .map(gm -> new GroupListResponse(
                        gm.getGroup().getGroupId(),
                        gm.getGroup().getName(),
                        gm.getGroup().getCategory().name(),
                        gm.getRole().name()
                ))
                .toList();
    }

    public List<PostResponse> getGroup(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group Not Found"));

        List<Post> posts = postRepository.findAllByGroup(group);

        return posts.stream().map(post -> {
            List<String> fileUrls = fileRepository.findAllByPost(post).stream()
                    .map(File::getFileUrl)
                    .toList();

            List<String> participantNames = participantRepository.findAllByPost(post).stream()
                    .map(p -> p.getGroupMember().getMember().getName())
                    .toList();

            return PostResponse.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .fileUrls(fileUrls)
                    .participantNames(participantNames)
                    .createdAt(post.getCreatedAt().toString()) // LocalDateTime → ISO 문자열
                    .build();
        }).toList();
    }

    public List<GroupMemberResponse> getGroupMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group Not Found"));

        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);

        return groupMembers.stream()
                .map(gm -> new GroupMemberResponse(
                        gm.getGroupMemberId(),
                        gm.getMember().getName(),
                        gm.getMember().getEmail(),
                        gm.getRole().name()
                ))
                .toList();
    }
}
