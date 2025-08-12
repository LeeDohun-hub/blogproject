package org.coupe.springbootdeveloper.dtopackage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.coupe.springbootdeveloper.domain.Article;
import org.coupe.springbootdeveloper.domain.Category;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private Category category;   // 이 필드를 추가하세요
    private String imageUrl;
    private LocalDateTime createdAt;
    private String author;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.category = article.getCategory();  // article의 카테고리 값 복사
        this.imageUrl = article.getImageUrl();
        this.createdAt = article.getCreatedAt();
        this.author = article.getAuthor();
    }
}
