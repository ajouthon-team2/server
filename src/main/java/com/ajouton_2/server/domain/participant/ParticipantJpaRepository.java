package com.ajouton_2.server.domain.participant;

import com.ajouton_2.server.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantJpaRepository extends JpaRepository<Participant, Long> {

    void deleteAllByPost(Post post);

}
