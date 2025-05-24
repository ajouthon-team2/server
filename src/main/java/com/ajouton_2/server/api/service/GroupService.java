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
    private final MemberJpaRepository memberRepository;
    private final GroupMemberJpaRepository groupMemberRepository;

    @Transactional
    public GroupInviteCodeResponse createGroup(GroupAddRequest request) {
        String uniqueCode = generateCode();

        Group group = Group.builder()
                .name(request.getName())
                .category(request.getCategory())
                .inviteCode(uniqueCode)
                .build();

        groupRepository.save(group);

    // member찾기

        GroupMember groupMember = GroupMember.builder()
                .group(group)
                .role(GroupMember.Role.LEADER)
//                .member(member)
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return groupMemberRepository.findByMember(member).stream()
                .map(gm -> new GroupListResponse(
                        gm.getGroup().getGroupId(),
                        gm.getGroup().getName(),
                        gm.getGroup().getCategory().name(),
                        gm.getRole().name()
                ))
                .toList();
    }
}
