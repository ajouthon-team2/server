package com.ajouton_2.server.domain.file;

import com.ajouton_2.server.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileJpaRepository extends JpaRepository<File, Long> {

    void deleteAllByPost(Post post);

    List<File> findAllByPost(Post post);
}
