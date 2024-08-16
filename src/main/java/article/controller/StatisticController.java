package article.controller;

import article.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class StatisticController {
    private final ArticleServiceImpl articleService;

    @Autowired
    public StatisticController(ArticleServiceImpl articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/statistics")
    public Map<LocalDate, Long> getArticleStatistics() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);
        return articleService.getDailyArticleCounts(startDate, endDate);
    }

}
