package pl.aplazuk.homewrok7news.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import pl.aplazuk.homewrok7news.aspect.CleanDataAspect;
import pl.aplazuk.homewrok7news.dao.NewsDao;
import pl.aplazuk.homewrok7news.model.Article;
import pl.aplazuk.homewrok7news.model.PaginatedArticleList;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {


    private static final String SPACE_FLIGHT_NEWS_URL = "https://api.spaceflightnewsapi.net/v4/articles/";
    private final RestClient restClient = RestClient.create(SPACE_FLIGHT_NEWS_URL);

    private final NewsDao newsDao;

    public NewsService(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @CleanDataAspect
    @Scheduled(cron = "0 0 14 ? * MON-FRI")
    public List<Article> getNewestArticles() {
        List<Article> articles = new ArrayList<>();
        ResponseEntity<PaginatedArticleList> spaceFlightNews = getSpaceFlightNews(10, OffsetDateTime.now());
        if (spaceFlightNews != null && spaceFlightNews.getStatusCode().is2xxSuccessful()) {
            articles = spaceFlightNews.getBody().getResults();
            newsDao.saveAll(articles);
        }
        return articles;
    }

    public List<Article> getAllArticles() {
        return newsDao.getAll();
    }

    public Optional<Article> getArticleById(int id) {
        return newsDao.getById(id);
    }

    public boolean updateArticle(Article article) {
        return newsDao.update(article);
    }

    public ResponseEntity<PaginatedArticleList> getSpaceFlightNews(Integer limit, OffsetDateTime publishedAt) {
        return restClient.get()
                .uri(getSpaceFlightNewsUri(limit, publishedAt))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    ResponseEntity.status(response.getStatusCode()).body(response.getStatusText());
                }))
                .toEntity(PaginatedArticleList.class);
    }

    private URI getSpaceFlightNewsUri(Integer limit, OffsetDateTime publishedAt) {
        return UriComponentsBuilder.fromHttpUrl(SPACE_FLIGHT_NEWS_URL)
                .queryParam("limit", limit)
                .queryParam("published_at", publishedAt)
                .build()
                .toUri();
    }
}
