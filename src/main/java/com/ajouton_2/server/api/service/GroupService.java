package com.ajouton_2.server.api.service;

import com.ajouton_2.server.api.dto.group.GroupAddRequest;
import com.ajouton_2.server.api.dto.group.GroupInviteCodeResponse;
import com.ajouton_2.server.api.dto.group.GroupListResponse;
import com.ajouton_2.server.domain.group.Group;
import com.ajouton_2.server.domain.group.GroupJpaRepository;
import com.ajouton_2.server.domain.groupmember.GroupMember;
import com.ajouton_2.server.domain.groupmember.GroupMemberJpaRepository;
import com.ajouton_2.server.domain.member.Member;
import com.ajouton_2.server.domain.member.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupJpaRepository groupRepository;
    private final GroupMemberJpaRepository groupMemberRepository;
    private final MemberService memberService;

    @Transactional
    public GroupInviteCodeResponse createGroup(GroupAddRequest request) {
        String uniqueCode = generateCode();

        Group group = Group.builder()
                .name(request.getName())
                .category(request.getCategory())
                .inviteCode(uniqueCode)
                .build();

        groupRepository.save(group);

        Member member = memberService.getLoginedMemnber();

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
        Member member = memberService.getLoginedMemnber();

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
}
