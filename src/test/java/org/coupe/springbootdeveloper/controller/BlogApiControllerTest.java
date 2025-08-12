package org.coupe.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.coupe.springbootdeveloper.domain.Article;
import org.coupe.springbootdeveloper.domain.Category;
import org.coupe.springbootdeveloper.dtopackage.AddArticleRequest;
import org.coupe.springbootdeveloper.dtopackage.UpdateArticleRequest;
import org.coupe.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

@DisplayName("addArticle: 블로그 글 추가에 성공한다.")
@Test
public void addArticle() throws Exception {
    // given
    final String url = "/api/articles";
    final String title = "title";
    final String content = "content";
    final String imageUrl = "https://example.com/image.jpg"; // 추가
    final AddArticleRequest userRequest = new AddArticleRequest(title, content, Category.DEV, imageUrl);

    final String requestBody = objectMapper.writeValueAsString(userRequest);

    // when
    ResultActions result = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody));


    // then
    result.andExpect(status().isCreated());
    List<Article> articles = blogRepository.findAll();
    assertThat(articles.size()).isEqualTo(1);
    assertThat(articles.get(0).getTitle()).isEqualTo(title);
    assertThat(articles.get(0).getContent()).isEqualTo(content);
}

@DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
@Test
public void findAllArticles() throws Exception {
    // given
    final String url = "/api/articles";
    final String title = "title";
    final String content = "content";
    blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .category(Category.DEV) // 누락된 필수 필드
            .imageUrl("https://example.com/image.jpg") // 선택 필드지만 있으면 좋음
            .build());

    // when
    final ResultActions resultActions = mockMvc.perform(get(url)
            .accept(MediaType.APPLICATION_JSON));

    // then
    resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value(content))
            .andExpect(jsonPath("$[0].title").value(title));
}


@DisplayName("findArticle: 블로그 글 조회에 성공한다.")
@Test
public void findArticle() throws Exception {
    // given
    final String url = "/api/articles/{id}";
    final String title = "title";
    final String content = "content";
    Article savedArticle = blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .category(Category.DEV) // 누락된 필수 필드
            .imageUrl("https://example.com/image.jpg") // 선택 필드지만 있으면 좋음
            .build());

    // when
    final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.
            getId()));

    // then
    resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(content))
            .andExpect(jsonPath("$.title").value(title));
}

@DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
@Test
public void deleteArticle() throws Exception {
    // given
    final String url = "/api/articles/{id}";
    final String title = "title";
    final String content = "content";
    Article savedArticle = blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .category(Category.DEV) // 누락된 필수 필드
            .imageUrl("https://example.com/image.jpg") // 선택 필드지만 있으면 좋음
            .build());

    // when
    mockMvc.perform(delete(url, savedArticle.getId()))
            .andExpect(status().isOk());

    // then
    List<Article> articles = blogRepository.findAll();
    assertThat(articles).isEmpty();
}

@DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
@Test
public void updateArticle() throws Exception {
    // given
    final String url = "/api/articles/{id}";
    final String title = "title";
    final String content = "content";
    Article savedArticle = blogRepository.save(Article.builder()
            .title(title)
            .content(content)
            .category(Category.DEV) // 누락된 필수 필드
            .imageUrl("https://example.com/image.jpg") // 선택 필드지만 있으면 좋음
            .build());
    final String newTitle = "new title";
    final String newContent = "new content";
    UpdateArticleRequest request = new UpdateArticleRequest(newTitle,
            newContent, Category.DEV, savedArticle.getImageUrl());

    // when
    ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)));

    // then
    result.andExpect(status().isOk());
    Article article = blogRepository.findById(savedArticle.getId()).get();
    assertThat(article.getTitle()).isEqualTo(newTitle);
    assertThat(article.getContent()).isEqualTo(newContent);
}
}