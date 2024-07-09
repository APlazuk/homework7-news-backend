package pl.aplazuk.homewrok7news.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import pl.aplazuk.homewrok7news.dao.NewsDao;
import pl.aplazuk.homewrok7news.model.Article;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
public class NewsAspect {

    private final NewsDao newsDao;

    public NewsAspect(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Before("@annotation(CleanDataAspect)")
    private void cleanDataBeforeSaveAll() {
        List<Article> all = newsDao.getAll();
        if (!all.isEmpty()) {
            List<Integer> articleIds = all.stream().map(article -> article.getId()).collect(Collectors.toList());
            newsDao.deleteAll(articleIds);
        }
    }
}
