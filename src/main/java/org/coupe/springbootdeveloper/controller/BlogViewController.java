package org.coupe.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.coupe.springbootdeveloper.domain.Article;
import org.coupe.springbootdeveloper.domain.Category;
import org.coupe.springbootdeveloper.dtopackage.ArticleListViewResponse;
import org.coupe.springbootdeveloper.dtopackage.ArticleViewResponse;
import org.coupe.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class BlogViewController {
    private final BlogService blogService;

    // 메인(전체 글)
    @GetMapping("/")
    public String index(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", Category.values());
        return "articleList";   // 카드형 목록 페이지
    }

    // 카테고리별 글 목록
    @GetMapping("/category/{category}")
    public String category(@PathVariable Category category, Model model) {
        List<ArticleListViewResponse> articles = blogService.findByCategory(category).stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("category", category);
        model.addAttribute("categories", Category.values()); // 카테고리 목록 추가
        return "index";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        model.addAttribute("categories", Category.values()); //글 작성할 때 카테고리 선택용
        return "newArticle";
    }
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", Category.values());
        return "articleList";  // articleList.html 뷰 템플릿을 반환
    }
}

