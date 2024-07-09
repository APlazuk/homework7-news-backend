package pl.aplazuk.homewrok7news.dao;


import pl.aplazuk.homewrok7news.model.Article;

import java.util.List;
import java.util.Optional;

public interface NewsDao {

    void saveAll(List<Article> articles);
    void deleteAll(List<Integer> ids);
    List<Article> getAll();
    Optional<Article> getById(int id);
    boolean update(Article article);

}
