package article.repository;

import article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByPublishDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
