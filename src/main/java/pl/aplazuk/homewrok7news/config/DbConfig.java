package pl.aplazuk.homewrok7news.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DbConfig implements ApplicationRunner {

    private DataSource dataSource;

    public DbConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initTables();
    }

    public void initTables() {
        String sql = "CREATE TABLE IF NOT EXISTS articles(article_id int," +
                " title VARCHAR(255), " +
                "url VARCHAR(2083), " +
                "image VARCHAR(2083), " +
                "news_site VARCHAR(255), " +
                "summary VARCHAR(2000), " +
                "published_at TIMESTAMP, PRIMARY KEY (article_id))";
        getJdbcTemplate().update(sql);
    }
}
