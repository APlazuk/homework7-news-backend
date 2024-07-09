package pl.aplazuk.homewrok7news.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.aplazuk.homewrok7news.model.Article;
import pl.aplazuk.homewrok7news.service.NewsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/news")
@CrossOrigin(value = "http://localhost:4200")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> findById(@PathVariable int id) {
        Optional<Article> optionalArticle = newsService.getArticleById(id);
        return optionalArticle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> findAll() {
        List<Article> allNewArticles = newsService.getAllArticles();
        return !allNewArticles.isEmpty() ? ResponseEntity.ok(allNewArticles) : ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/latest-articles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> getAllLatestArticles() {
        List<Article> allNewArticles = newsService.getNewestArticles();
        return !allNewArticles.isEmpty() ? ResponseEntity.ok(allNewArticles) : ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> editArticle(@RequestBody @Valid Article article) {
        boolean updatedArticle = newsService.updateArticle(article);
        return updatedArticle ? ResponseEntity.ok(article) : ResponseEntity.badRequest().build();
    }
}
