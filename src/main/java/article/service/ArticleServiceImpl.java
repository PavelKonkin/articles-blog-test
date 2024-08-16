package article.service;

import article.model.Article;
import article.model.dto.ArticleDto;
import article.mapper.ArticleMapper;
import article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    public ArticleDto createArticle(ArticleDto articleDto) {
        Article article = articleMapper.convertArticleDto(articleDto);
        return articleMapper.convertArticle(articleRepository.save(article));
    }

    public List<ArticleDto> listArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).toList().stream()
                .map(articleMapper::convertArticle)
                .collect(Collectors.toList());
    }

    public Map<LocalDate, Long> getDailyArticleCounts(LocalDateTime startDate, LocalDateTime endDate) {
        List<Article> articles = articleRepository.findByPublishDateBetween(startDate, endDate);
        return articles.stream()
                .collect(Collectors.groupingBy(article -> article.getPublishDate().toLocalDate(), Collectors.counting()));
    }
}
