package article.service;

import article.model.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface ArticleService {
    public ArticleDto createArticle(ArticleDto article);


    public Page<ArticleDto> listArticles(Pageable pageable);

    public Map<LocalDate, Long> getDailyArticleCounts(LocalDateTime startDate, LocalDateTime endDate);
}