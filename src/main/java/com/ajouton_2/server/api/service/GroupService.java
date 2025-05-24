//package com.ajouton_2.server.api.service;
//
//import com.ajouton_2.server.api.dto.group.GroupAddRequest;
//import com.ajouton_2.server.api.dto.group.GroupAddResponse;
//import com.ajouton_2.server.domain.group.Group;
//import com.ajouton_2.server.domain.group.GroupJpaRepository;
//import com.ajouton_2.server.domain.groupmember.GroupMember;
//import com.ajouton_2.server.domain.groupmember.GroupMemberJpaRepository;
//import com.ajouton_2.server.domain.member.MemberJpaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.security.SecureRandom;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class GroupService {
//
//    private final GroupJpaRepository groupRepository;
//    private final MemberJpaRepository memberRepository;
//    private final GroupMemberJpaRepository groupMemberRepository;
//
//    @Transactional
//    public GroupAddResponse createGroup(GroupAddRequest request) {
//        String uniqueCode = generateCode();
//
//        Group group = Group.builder()
//                .name(request.getName())
//                .category(request.getCategory())
//                .inviteCode(uniqueCode)
//                .build();
//
//        groupRepository.save(group);
//
//    // member찾기
//
//        GroupMember groupMember = GroupMember.builder()
//                .group(group)
//                .role(GroupMember.Role.LEADER)
////                .member(member)
//                .build();
//        groupMemberRepository.save(groupMember);
//
//        return GroupAddResponse.builder()
//                .message("생성 성공")
//                .inviteCode(group.getInviteCode())
//                .build();
//    }
//
//    private String generateCode() {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder sb = new StringBuilder(6);
//        SecureRandom random = new SecureRandom();
//
//        for (int i = 0; i < 6; i++) {
//            sb.append(characters.charAt(random.nextInt(characters.length())));
//        }
//        return sb.toString();
//    }
//}
