package org.coupe.springbootdeveloper.dtopackage;

import lombok.Getter;
import org.coupe.springbootdeveloper.domain.Article;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }

}
