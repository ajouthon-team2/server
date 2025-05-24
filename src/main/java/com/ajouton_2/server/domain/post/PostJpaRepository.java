package com.ajouton_2.server.domain.post;

import com.ajouton_2.server.domain.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByGroup(Group group);
}