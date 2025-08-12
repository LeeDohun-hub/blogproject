package org.coupe.springbootdeveloper.dtopackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.coupe.springbootdeveloper.domain.Category;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {
    private String title;
    private String content;
    private Category category; // 필요하다면 추가 (카테고리 수정도 가능하게 할 경우)
    private String imageUrl;
}
