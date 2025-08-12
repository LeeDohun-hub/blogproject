package org.coupe.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.coupe.springbootdeveloper.domain.Article;
import org.coupe.springbootdeveloper.domain.Category;
import org.coupe.springbootdeveloper.dtopackage.AddArticleRequest;
import org.coupe.springbootdeveloper.dtopackage.ArticleListViewResponse;
import org.coupe.springbootdeveloper.dtopackage.ArticleViewResponse;
import org.coupe.springbootdeveloper.repository.BlogRepository;
import org.coupe.springbootdeveloper.service.BlogService;
import org.coupe.springbootdeveloper.util.S3Uploader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BlogViewController {
    private final BlogService blogService;
    private final S3Uploader s3Uploader;

    // 메인(전체 글)
    @GetMapping("/")
    public String index(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream   ()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", Category.values());
        return "articleList";   // 카드형 목록 페이지
    }

    // 카테고리별 글 목록
    @GetMapping("/category/{category}")
    public String category(@PathVariable Category category, Model model) {
        List<ArticleListViewResponse> articles = blogService.findByCategory(category)
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("category", category);
        model.addAttribute("categories", Category.values()); // 카테고리 목록 추가
        // category 값에 따라 전용 페이지 반환
        return "category/" + category.name().toLowerCase();

    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model, Authentication authentication) {
        System.out.println("Authentication: " + authentication);
        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
        }
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        model.addAttribute("categories", Category.values()); // ✅ 카테고리 추가

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
    public String getArticles(Model model, Authentication authentication) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", Category.values());
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("username", null);
        }
        return "articleList";  // articleList.html 뷰 템플릿을 반환
    }
    @PostMapping("/articles")
    public String createArticle(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("category") Category category,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = s3Uploader.upload(image, "uploads");  // S3 업로드
            } catch (IOException e) {
                model.addAttribute("errorMessage", "이미지 업로드 실패: " + e.getMessage());
                return "newArticle";
            }
        }

        AddArticleRequest request = new AddArticleRequest(title, content, category, imageUrl);
        // 현재 로그인한 사용자 이름 가져오기
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        blogService.save(request, userName); // 두 번째 인자로 전달

        return "redirect:/articles";
    }


}

