package com.ajouton_2.server.domain.groupmember;

import com.ajouton_2.server.domain.group.Group;
import com.ajouton_2.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberJpaRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupAndMember(Group group, Member member);

    List<GroupMember> findByMemberMemberId(Long memberId);

    List<GroupMember> findByGroup(Group group);
}
