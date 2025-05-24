package com.ajouton_2.server.api.service;

import com.ajouton_2.server.api.dto.groupmember.GroupSignInRequest;
import com.ajouton_2.server.domain.group.Group;
import com.ajouton_2.server.domain.group.GroupJpaRepository;
import com.ajouton_2.server.domain.groupmember.GroupMember;
import com.ajouton_2.server.domain.groupmember.GroupMemberJpaRepository;
import com.ajouton_2.server.domain.member.Member;
import com.ajouton_2.server.domain.member.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

    private final GroupJpaRepository groupRepository;
    private final MemberService memberService;
    private final GroupMemberJpaRepository groupMemberRepository;

    public void signInToGroup(GroupSignInRequest request) {
        // 초대 코드로 그룹 조회
        Group group = groupRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new IllegalArgumentException("Group Not Found"));

        // 사용자 조회
        Member member = memberService.getLoginedMemnber();

        // 이미 가입된 경우 방지
        if (groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new IllegalStateException("이미 가입된 그룹입니다.");
        }

        // 그룹 멤버 생성
        GroupMember groupMember = GroupMember.builder()
                .group(group)
                .member(member)
                .role(GroupMember.Role.MEMBER)
                .build();

        groupMemberRepository.save(groupMember);
    }
}