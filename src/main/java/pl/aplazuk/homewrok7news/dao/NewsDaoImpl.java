package pl.aplazuk.homewrok7news.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.aplazuk.homewrok7news.model.Article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class NewsDaoImpl implements NewsDao {

    private final JdbcTemplate jdbcTemplate;

    public NewsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void saveAll(List<Article> articles) {
        String sql = "INSERT INTO articles VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, articles, 50,
                new ParameterizedPreparedStatementSetter<Article>() {
                    @Override
                    public void setValues(PreparedStatement ps, Article article) throws SQLException {
                        ps.setLong(1, article.getId());
                        ps.setString(2, article.getTitle());
                        ps.setString(3, article.getUrl().toASCIIString());
                        ps.setString(4, article.getImageUrl().toASCIIString());
                        ps.setString(5, article.getNewsSite());
                        ps.setString(6, article.getSummary());
                        ps.setTimestamp(7, Timestamp.valueOf(article.getPublishedAt().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
                    }
                });
    }

    @Override
    @Transactional
    public void deleteAll(List<Integer> ids) {
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("DELETE FROM articles WHERE article_id IN (%s)", inSql);
        jdbcTemplate.update(sql, ids.toArray());
    }

    @Override
    public List<Article> getAll() {
        String sql = "SELECT * FROM articles";
        List<Article> query = jdbcTemplate.query(sql, new RowMapper<Article>() {
            @Override
            public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Article(
                        rs.getInt("article_id"),
                        rs.getString("title"),
                        rs.getString("url"),
                        rs.getString("image"),
                        rs.getString("news_site"),
                        rs.getString("summary"),
                        rs.getTimestamp("published_at").toLocalDateTime());
            }
        });
        return query;
    }

    @Override
    public Optional<Article> getById(int id) {
        String sql = "SELECT * FROM articles WHERE article_id = ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<Optional<Article>>() {
            @Override
            public Optional<Article> mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Optional.of(new Article(
                        rs.getInt("article_id"),
                        rs.getString("title"),
                        rs.getString("url"),
                        rs.getString("image"),
                        rs.getString("news_site"),
                        rs.getString("summary"),
                        rs.getTimestamp(7).toLocalDateTime()));
            }
        }, id);
    }

    @Override
    public boolean update(Article article) {
        String sql = "UPDATE articles SET title = ?, url = ?, image = ?, news_site = ?, summary = ?, published_at = ? WHERE article_id = ?";
        int updatedRows = jdbcTemplate.update(sql, article.getTitle(), article.getUrl().toASCIIString(),
                article.getImageUrl().toASCIIString(), article.getNewsSite(), article.getSummary(),
                Timestamp.valueOf(article.getPublishedAt().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()), article.getId());
        return updatedRows == 1;
    }
}
