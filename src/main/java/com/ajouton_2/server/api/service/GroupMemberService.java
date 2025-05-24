//package com.ajouton_2.server.api.service;
//
//import com.ajouton_2.server.api.dto.groupmember.GroupSignInRequest;
//import com.ajouton_2.server.api.dto.groupmember.GroupSignInResponse;
//import com.ajouton_2.server.domain.group.GroupJpaRepository;
//import com.ajouton_2.server.domain.groupmember.GroupMemberJpaRepository;
//import com.ajouton_2.server.domain.member.MemberJpaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class GroupMemberService {
//
//    private final GroupJpaRepository groupRepository;
//    private final MemberJpaRepository memberRepository;
//    private final GroupMemberJpaRepository groupMemberRepository;
//
//    public GroupSignInResponse signInToGroup(GroupSignInRequest request) {
//        return true;
//    }
//
//}