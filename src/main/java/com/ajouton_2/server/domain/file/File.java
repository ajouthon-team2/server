package com.ajouton_2.server.domain.file;

import com.ajouton_2.server.domain.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public File(Post post, String fileName, String fileUrl, LocalDateTime createdAt) {
        this.post = post;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
    }
}
