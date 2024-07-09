package pl.aplazuk.homewrok7news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Homewrok7NewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Homewrok7NewsApplication.class, args);
    }

}
