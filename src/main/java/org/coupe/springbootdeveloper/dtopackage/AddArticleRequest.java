package org.coupe.springbootdeveloper.dtopackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.coupe.springbootdeveloper.domain.Article;
import org.coupe.springbootdeveloper.domain.Category;

@NoArgsConstructor //기본생성자 추가
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
    private String title;
    private String content;
    private Category category; // 추가
    private String imageUrl;  // 썸네일 이미지 URL

    public Article toEntity(String author) { //생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .category(category) // 추가
                .imageUrl(imageUrl)
                .build();
    }
}
