package article.controller;

import article.model.dto.ArticleDto;
import article.service.ArticleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@Slf4j
@Validated
public class ArticleController {
    private final ArticleServiceImpl articleService;

    @Autowired
    public ArticleController(ArticleServiceImpl articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ArticleDto createArticle(@RequestBody @Valid ArticleDto article) {
        return articleService.createArticle(article);
    }

    @GetMapping
    public List<ArticleDto> listArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleService.listArticles(pageable);
    }
}
