package com.ajouton_2.server.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
