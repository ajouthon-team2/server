package com.ajouton_2.server.domain.post;

import com.ajouton_2.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
}