package article.mapper;

import article.model.Article;
import article.model.dto.ArticleDto;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {
    public Article convertArticleDto(ArticleDto articleDto) {
        return Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .author(articleDto.getAuthor())
                .publishDate(articleDto.getPublishDate())
                .build();
    }

    public ArticleDto convertArticle(Article article) {
        return ArticleDto.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .publishDate(article.getPublishDate())
                .build();
    }
}
