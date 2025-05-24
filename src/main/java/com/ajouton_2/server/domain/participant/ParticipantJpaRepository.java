package com.ajouton_2.server.domain.participant;

import com.ajouton_2.server.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantJpaRepository extends JpaRepository<Participant, Long> {

    void deleteAllByPost(Post post);

    List<Participant> findAllByPost(Post post);

    long countByPost(Post post);
}
