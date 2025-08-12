package org.coupe.springbootdeveloper.repository;

import org.coupe.springbootdeveloper.domain.Article;
import org.coupe.springbootdeveloper.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BlogRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByCategoryOrderByCreatedAtDesc(Category category);
}
