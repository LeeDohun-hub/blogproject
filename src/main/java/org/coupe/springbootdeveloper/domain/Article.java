package org.coupe.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Article {

    @Id //id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    // 🔹 카테고리 추가
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(nullable = true) // 또는 이 줄 아예 삭제 (기본은 nullable=true)
    private String imageUrl;  // ← 이미지 URL 저장용 필드 추가

    @Builder //빌더 패턴으로 객체 생성
    public Article(String author, String title, String content, Category category, String imageUrl) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category; // 추가
        this.imageUrl = imageUrl;

    }
    public void update(String title, String content, Category category, String imageUrl) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.imageUrl = imageUrl;
    }
}
